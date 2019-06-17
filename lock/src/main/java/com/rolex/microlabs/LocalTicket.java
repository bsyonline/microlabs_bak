/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class LocalTicket {

    static JedisPool jedisPool;

    static {
        String host = "localhost";
        int port = 6379;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        jedisPool = new JedisPool(config, host, port);
    }

    public void reduce(int num) {
        Jedis jedis = jedisPool.getResource();
        Integer tickets = Integer.parseInt(jedis.get("ticket"));
        boolean buyTicket = false;
        if (tickets - num >= 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tickets = tickets - num;
            jedis.set("ticket", String.valueOf(tickets));
            log.info("用户{}买到1张票,还剩{}张票", Thread.currentThread().getId(), tickets);
            buyTicket = true;
        } else {
            log.info("余票不足,用户{}没有买到票", Thread.currentThread().getId());
        }
        if (buyTicket) {
            if (Thread.currentThread().getId() % 2 == 0) {
                // vip加500积分
                log.info("用户{}是VIP,获得500积分", Thread.currentThread().getId());
            }
        }
    }

    //-----------------------------------------------------------------------------

    public void reduceWithSynchronized(int num) {
        Jedis jedis = jedisPool.getResource();
        boolean buyTicket = false;
        synchronized (this) {
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tickets = tickets - num;
                jedis.set("ticket", String.valueOf(tickets));
                log.info("用户{}买到1张票,还剩{}张票", Thread.currentThread().getId(), tickets);
                buyTicket = true;
            } else {
                log.info("余票不足,用户{}没有买到票", Thread.currentThread().getId());
            }
        }
        if (buyTicket) {
            if (Thread.currentThread().getId() % 2 == 0) {
                // vip加500积分
                log.info("用户{}是VIP,获得500积分", Thread.currentThread().getId());
            }
        }
    }

    //-----------------------------------------------------------------------------

    Lock lock = new ReentrantLock();

    public void reduceWithLock(int num) {
        boolean buyTicket = false;
        try {
            Jedis jedis = jedisPool.getResource();
            lock.lock();
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tickets = tickets - num;
                jedis.set("ticket", String.valueOf(tickets));
                log.info("用户{}买到1张票,还剩{}张票", Thread.currentThread().getId(), tickets);
                buyTicket = true;
            } else {
                log.info("余票不足,用户{}没有买到票", Thread.currentThread().getId());
            }

        } finally {
            lock.unlock();
        }
        if (buyTicket) {
            if (Thread.currentThread().getId() % 2 == 0) {
                // vip加500积分
                log.info("用户{}是VIP,获得500积分", Thread.currentThread().getName());
            }
        }
    }

    //--------------------------------------------------------------------


    public static void main(String[] args) {
        LocalTicket localTicket = new LocalTicket();
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    localTicket.reduce(1);
//                    localTicket.reduceWithSynchronized(1);
                    localTicket.reduceWithLock(1);
                }
            }).start();
            latch.countDown();
        }
    }
}
