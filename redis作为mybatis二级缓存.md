### redis作为mybatis的二级缓存

##### 1.什么是二级缓存

之所以称之为“二级缓存”，是相对于“一级缓存”而言的。既然有了一级缓存，那么为什么要提供二级缓存呢？我们知道，在一级缓存中，不同session进行相同SQL查询的时候，是查询两次数据库的。显然这是一种浪费，既然SQL查询相同，就没有必要再次查库了，直接利用缓存数据即可，这种思想就是MyBatis二级缓存的初衷。

另外，Spring和MyBatis整合时，每次查询之后都要进行关闭sqlsession，关闭之后数据被清空。所以MyBatis和Spring整合之后，一级缓存是没有意义的。如果开启二级缓存，关闭sqlsession后，会把该sqlsession一级缓存中的数据添加到mapper namespace的二级缓存中。这样，缓存在sqlsession关闭之后依然存在。

##### 2.怎么开启二级缓存

1.导入jar包

```text
   <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.79</version>
        </dependency>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>
```

2.在properties文件中配置连接redis

```properties
# Redis服务器地址
spring.redis.host=192.168.31.5
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.maxTotal=20
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.maxWaitMillis=-1
# 连接池中的最大空闲连接
spring.redis.maxIdle=10
# 连接池中的最小空闲连接
spring.redis.minIdle=0
```

##### 3.配置redis连接

3.1创建实体类，获取配置文件信息

```java
@ConfigurationProperties(prefix = MyRedisProperties.REDIS_PREFIX)
@Data
@Component
public class MyRedisProperties {
    public static final String REDIS_PREFIX = "spring.redis";
    private Integer database;
    private String host;
    private Integer port;
    private String password;
    private Integer maxTotal;
    private Integer maxWaitMillis;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer timeout;

}
```

3.2 配置redis连接，以及jedis连接池

```java
@Configuration
public class RedisConfig {
    /**
     * jedis连接池
     * @param standaloneConfig redis
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration standaloneConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(standaloneConfig);
        return jedisConnectionFactory;
    }

    /**
     * 配置连接redis
     * @param properties
     * @return
     */
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(MyRedisProperties properties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(properties.getDatabase());
        redisStandaloneConfiguration.setHostName(properties.getHost());
        redisStandaloneConfiguration.setPort(properties.getPort());
        redisStandaloneConfiguration.setPassword(properties.getPassword());
        return redisStandaloneConfiguration;
    }

}
```



##### 4.在我们的mapper.xml文件中添加cache标签

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zzg.boot.mapper.StudentMapper">
    
    /*
    *type中填写你重写mybatis的cache类的位置
    */
    <cache type="com.zzg.boot.config.cache.StuCache">
    </cache>
    <resultMap id="BaseResultMap" type="com.zzg.boot.pojo.entity.Student">
        <!--@mbg.generated-->
        <!--@Table student-->
        <result column="id" jdbcType="VARCHAR" property="id"/>
        <result column="S_name" jdbcType="VARCHAR" property="sName"/>
        <result column="S_age" jdbcType="TIMESTAMP" property="sAge"/>
        <result column="S_sex" jdbcType="VARCHAR" property="sSex"/>
    </resultMap>
    <sql id="Base_Column_List">
        <!--@mbg.generated-->
        id, S_name, S_age, S_sex
    </sql>

    <insert id="insert" parameterType="com.zzg.boot.pojo.entity.Student">
        <!--@mbg.generated-->
        insert into student (id, S_name, S_age,
                             S_sex)
        values (#{id,jdbcType=VARCHAR}, #{sName,jdbcType=VARCHAR}, #{sAge,jdbcType=TIMESTAMP},
                #{sSex,jdbcType=VARCHAR})
    </insert>
    <insert id="insertSelective" parameterType="com.zzg.boot.pojo.entity.Student">
        <!--@mbg.generated-->
        insert into student
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                id,
            </if>
            <if test="sName != null">
                S_name,
            </if>
            <if test="sAge != null">
                S_age,
            </if>
            <if test="sSex != null">
                S_sex,
            </if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <if test="id != null">
                #{id,jdbcType=VARCHAR},-
            </if>
            <if test="sName != null">
                #{sName,jdbcType=VARCHAR},
            </if>
            <if test="sAge != null">
                #{sAge,jdbcType=TIMESTAMP},
            </if>
            <if test="sSex != null">
                #{sSex,jdbcType=VARCHAR},
            </if>
        </trim>
    </insert>

    <select id="selectAllStudent" resultMap="BaseResultMap">
        select id, S_name, S_age, S_sex
        from student
    </select>
</mapper>
```



##### 5.重写mybatis的cache方法

```java
/**
 * 重写cache方法
 */
public class StuCache implements Cache {


    private String id;

    private static final String MYBATIS_KEY = "mybatis";


    private RedisConnection getConnection() {
        JedisConnectionFactory bean = SpringUtil.getBean(JedisConnectionFactory.class);
        RedisConnection connection = bean.getConnection();
        return connection;
    }

    public StuCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * mybatis 缓存数据的方法  mybatis从数据库中查询到数据之后 会去调用这个方法
     * 我们需要在这个方法中 定制 缓存数据存储到哪 --- redis
     */
    @Override
    public void putObject(Object key, Object value) {

        RedisConnection connection = getConnection();

        byte[] keyByte = (MYBATIS_KEY + key.toString()).getBytes();
        byte[] valueByte = RedisUtity.serialize(value);

        connection.set(keyByte, valueByte);
        connection.close();
    }

    /**
     * mybatis 查询数据库之前 先根据key 查询缓存中的数据
     * 我们需要在这个函数中 根据key  查询redis中的数据
     */
    @Override
    public Object getObject(Object key) {
        RedisConnection connection = getConnection();
        byte[] keyByte = (MYBATIS_KEY + key.toString()).getBytes();
        byte[] valueByte = connection.get(keyByte);
        Object deserialize = RedisUtity.serialize(key);
        connection.close();
        return deserialize;
    }

    /**
     * 根据key 删除指定的 缓存
     */
    @Override
    public Object removeObject(Object key) {

        RedisConnection connection = getConnection();
        byte[] keyByte = (MYBATIS_KEY + key.toString()).getBytes();
        Boolean expire = connection.expire(keyByte, 0);
        connection.close();

        return expire;
    }

    /**
     * 清空缓存  mybatis在 DML操作的时候 会去调用这个函数
     * 以前 是这样清空的connection.flushAll();
     * 但是有问题  因为以后redis 除了存储缓存 还要存储令牌 如果全清 令牌也没有了
     */
    @Override
    public void clear() {

        RedisConnection connection = getConnection();

        Cursor<byte[]> scan = connection.scan(new ScanOptions.ScanOptionsBuilder().count(1000).match(MYBATIS_KEY + "*").build());

        while (scan.hasNext()) {
            byte[] keyByte = scan.next();
            connection.del(keyByte);
        }

        connection.close();

    }

    /**
     * 获取缓存大小
     */
    @Override
    public int getSize() {

        RedisConnection connection = getConnection();
        Long aLong = connection.dbSize();
        connection.close();

        return aLong.intValue();
    }
}
```

历时文档[张中贵/boot (gitee.com)](https://gitee.com/zhang-zhonggui/boot)
