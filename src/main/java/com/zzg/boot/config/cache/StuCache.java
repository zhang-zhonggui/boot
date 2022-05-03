package com.zzg.boot.config.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.zzg.boot.utity.RedisUtity;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;


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

