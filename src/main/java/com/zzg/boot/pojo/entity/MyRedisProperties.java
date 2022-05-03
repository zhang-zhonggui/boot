package com.zzg.boot.pojo.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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