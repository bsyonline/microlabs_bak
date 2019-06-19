/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class DistributedTicket {

    @Autowired
    JedisPool jedisPool;
    RedisDistributedLock lock;

    @PostConstruct
    public void init() {
        lock = new RedisDistributedLock(jedisPool);
    }

    public void reduceWithJedisLock(int num) {
        String lockKey = "ticket_lock";
        Jedis jedis = jedisPool.getResource();
        try {
            lock.lock(lockKey);
            boolean buyTicket = false;
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
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
        } finally {
            lock.unlock(lockKey);
        }
    }

    @Autowired
    Redisson redisson;

    public void reduceWithRedissionLock(int num) {
        String lockKey = "ticket_lock";
        Jedis jedis = jedisPool.getResource();
        RLock lock = redisson.getLock(lockKey);
        try {
            lock.lock();
            boolean buyTicket = false;
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
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
        } finally {
            lock.unlock();
        }
    }
}
