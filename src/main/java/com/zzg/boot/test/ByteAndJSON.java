package com.zzg.boot.test;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.zzg.boot.pojo.entity.Student;
import com.zzg.boot.utity.RedisUtity;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/3 0:23
 */
public class ByteAndJSON {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "张中贵";
        System.out.println("张三");
        Student student = new Student(1, "张三", "18", "男");
        byte[] key = RedisUtity.serialize(student);
        byte[] value = RedisUtity.serialize(a);

        JedisConnectionFactory bean = SpringUtil.getBean(JedisConnectionFactory.class);
        RedisConnection connection = bean.getConnection();
        try {
            Boolean set = connection.set(key, value);
            if (set){
                byte[] bytes = connection.get(key);
                Student getStu = (Student) RedisUtity.deserialize(bytes);
                System.out.println(getStu);
            }else {
                System.out.println("失败了，再来一次");
            }
        } finally {
            connection.close();
        }


    }
}
