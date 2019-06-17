/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @author rolex
 * @since 2019
 */
public class RedisDistributedLock {

    ThreadLocal<String> context = new ThreadLocal<>();
    JedisPool jedisPool;
    Timer timer;

    public RedisDistributedLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void lock(String lockKey) {
        while (!tryLock(lockKey)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean tryLock(String lockKey, int expire) {
        String lockId = UUID.randomUUID().toString();
        context.set(lockId);
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(lockKey, lockId, "NX", "PX", expire);
        if ("OK".equals(result)) {
            int period = 10000 / 3;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    jedis.expire(lockKey, expire);
                }
            }, 0, period);
            return true;
        }
        return false;
    }

    public boolean tryLock(String lockKey) {
        return tryLock(lockKey, 30000);
    }

    public void unlock(String lockKey) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        jedisPool.getResource().eval(script, Collections.singletonList(lockKey), Collections.singletonList(context.get()));
        timer.cancel();
    }

}
