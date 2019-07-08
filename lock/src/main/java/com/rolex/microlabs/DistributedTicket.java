/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.lock.RedisDistributedLock;
import com.rolex.microlabs.lock.SimpleDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.locks.Lock;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class DistributedTicket {

    @Autowired
    JedisPool jedisPool;

    public void reduceWithJedisLock(int userId, int num) {
        String lockKey = "jedis_ticket_lock";
        Jedis jedis = jedisPool.getResource();
        Lock lock = new RedisDistributedLock(lockKey);
        try {
            lock.lock();
            boolean isBought = false;
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
                tickets = tickets - num;
                jedis.set("ticket", String.valueOf(tickets));
                log.info("用户{}买到1张票,还剩{}张票", userId, tickets);
                isBought = true;
            } else {
                log.info("余票不足,用户{}没有买到票", userId, tickets);
            }
            if (isBought && Thread.currentThread().getId() % 2 == 0) {
                // vip加积分
                log.info("用户{}是VIP，获得500积分", userId);
            }
        } finally {
            lock.unlock();
        }
    }

    @Autowired
    Redisson redisson;

    public void reduceWithRedissionLock(int userId, int num) {
        String lockKey = "redission_ticket_lock";
        Jedis jedis = jedisPool.getResource();
        RLock lock = redisson.getLock(lockKey);
        try {
            lock.lock();
            boolean isBought = false;
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
                tickets = tickets - num;
                jedis.set("ticket", String.valueOf(tickets));
                log.info("用户{}买到1张票,还剩{}张票", userId, tickets);
                isBought = true;
            } else {
                log.info("余票不足,用户{}没有买到票", userId, tickets);
            }
            if (isBought && Thread.currentThread().getId() % 2 == 0) {
                // vip加积分
                log.info("用户{}是VIP，获得500积分", userId);
            }
        } finally {
            lock.unlock();
        }
    }

    public void reduceWithZkLock(int userId, int num) {
        String lockKey = "/zk_ticket_lock";
        Jedis jedis = jedisPool.getResource();
        SimpleDistributedLock lock = new SimpleDistributedLock(lockKey);
        try {
            lock.lock();
            boolean isBought = false;
            Integer tickets = Integer.parseInt(jedis.get("ticket"));
            if (tickets - num >= 0) {
                tickets = tickets - num;
                jedis.set("ticket", String.valueOf(tickets));
                System.out.println("---------用户" + userId + "买到1张票,还剩" + tickets + "张票");
                isBought = true;
            } else {
                System.out.println("---------余票不足,用户" + userId + "没有买到票");
            }
            if (isBought && Thread.currentThread().getId() % 2 == 0) {
                // vip加积分
                System.out.println("---------用户" + userId + "是VIP，获得500积分");
            }
        } finally {
            lock.unlock();
        }
    }
}
