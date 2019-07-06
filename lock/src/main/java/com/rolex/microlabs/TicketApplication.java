/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootApplication
@RestController
public class TicketApplication {

    @Autowired
    DistributedTicket distributedTicket;

    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }

    @GetMapping("/tickets/{user_id}")
    public String ticket(@PathVariable("user_id") int userId) {
        distributedTicket.reduceWithJedisLock(userId, 1);
//        distributedTicket.reduceWithRedissionLock(userId, 1);
        return "OK";
    }

    @Bean
    public JedisPool jedisPool() {
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

    @Bean
    public Redisson redissonSentinel() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return (Redisson) Redisson.create(config);
    }

}
