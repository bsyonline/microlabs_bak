/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.dao;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;

/**
 * @author rolex
 * @since 2018
 */
@Repository
@Slf4j
public class RedisDao {

    @Autowired
    private JedisPool jedisPool;
    private static final String KEY_PREFIX = "prefix:";
    private RuntimeSchema schema = null;

    public <T> T get(String id, Class<T> clazz) {
        schema = RuntimeSchema.createFrom(clazz);
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = KEY_PREFIX + id;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    log.info("query redis hits: key={}", key);
                    T t = (T) schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, t, schema);
                    return t;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String put(String key, Object obj) {
        log.info("put obj into redis");
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String k = KEY_PREFIX + key;
                byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 失效时间使用随机数，防止缓存击穿
                int expire = 60 * 60 * (new Random().nextInt(10) + 1);
                String result = jedis.setex(k.getBytes(), expire, bytes);
                // String result = jedis.set(key.getBytes(), bytes); // 不带失效时间
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
