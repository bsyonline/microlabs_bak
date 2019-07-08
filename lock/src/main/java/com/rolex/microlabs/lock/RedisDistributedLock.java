/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.lock;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class RedisDistributedLock implements Lock {

    ThreadLocal<String> context = new ThreadLocal<>();
    static JedisPool jedisPool = jedisPool();
    int expire = 100;
    Timer timer;
    String lockKey;

    public RedisDistributedLock(String lockKey) {
        this.lockKey = lockKey;
    }

    public RedisDistributedLock(String lockKey, int expire) {
        this(lockKey);
        this.expire = expire;
    }

    @Override
    public void lock() {
        while (!tryLock()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        String lockId = UUID.randomUUID().toString();
        Jedis jedis = jedisPool.getResource();
        try {
            String result = jedis.set(lockKey, lockId, "NX", "PX", expire);
            if ("OK".equals(result)) {
                context.set(lockId);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Jedis jedis = jedisPool.getResource();
                        try {
                            jedis.expire(lockKey, expire / 1000);
                        } finally {
                            jedis.close();
                        }
                    }
                }, 0, expire / 3);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("lockKey={}", lockKey);
            return false;
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        Jedis jedis = jedisPool.getResource();
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            if (context.get() == null) {
                return;
            }
            jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(context.get()));
        } finally {
            context.remove();
            timer.cancel();
            jedis.close();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static JedisPool jedisPool() {
        String host = "localhost";
        int port = 6379;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return new JedisPool(config, host, port);
    }

}
