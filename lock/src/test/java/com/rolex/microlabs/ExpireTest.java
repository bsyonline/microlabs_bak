/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExpireTest {

    @Autowired
    JedisPool jedisPool;

    @Test
    public void testExpire(){
        String key = "qqq";
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, "qaz", "NX", "PX", 10000);
        while(true){
            jedis.expire(key,10);
            System.out.println("续租1次");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
