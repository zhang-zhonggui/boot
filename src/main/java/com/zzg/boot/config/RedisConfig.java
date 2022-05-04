package com.zzg.boot.config;

import com.zzg.boot.pojo.entity.MyRedisProperties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zzg
 * 通过jedis连接redis创建jedis连接池
 */
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
