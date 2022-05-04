package com.zzg.boot.test;

import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;

/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/3 0:23
 */
public class ByteAndJSON {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Jedis jedis = new Jedis("r-bp17rzme09z6b4i04ypd.redis.rds.aliyuncs.com");
        jedis.auth("Zhang1012");
        jedis.set("test", "张三");

    }
}