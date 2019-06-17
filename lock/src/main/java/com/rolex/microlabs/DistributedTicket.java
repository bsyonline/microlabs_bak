/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
@Slf4j
public class DistributedTicket {

    @Autowired
    JedisPool jedisPool;
    Timer expireTimer;

    public void reduce(int num) {
        String lockKey = "ticket_lock";
        String lockId = UUID.randomUUID().toString();
        Jedis jedis = jedisPool.getResource();
        try {
//            Long result = 0L;
//            while (0 == result) {
//                result = jedis.setnx(lockKey, lockId);
//                jedis.expire(lockKey, 10);
//            }
            String result = "";
            while (!"OK".equals(result)) {
                int expire = 10000;
                result = jedis.set(lockKey, lockId, "NX", "PX", 10000);
                int period = 10000 / 3;
                expireTimer = new Timer();
                expireTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        jedis.expire(lockKey, expire);
                    }
                }, 0, period);
            }
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
//            if (lockId.equals(jedis.get(lockKey))) {
//                jedis.del(lockKey);
//            }
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockId));
            expireTimer.cancel();
        }
    }
}
