package com.zzg.boot.utity;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/3 14:03
 */
public class RedisUtity {
    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] bs = baos.toByteArray();
            baos.close();
            oos.close();

            return bs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deserialize(byte[] bits) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bits);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();

            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
