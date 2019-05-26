/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author rolex
 * @since 2018
 */
@Component
@ConfigurationProperties(prefix = "redis.pool")
@Slf4j
public class RedisConfig {
    
    private String host;
    private int port;
    private String password;
    private int timeout;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
    
    @Bean
    public JedisPool getInstance() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout);
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        return pool;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public int getMaxTotal() {
        return maxTotal;
    }
    
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public int getMaxIdle() {
        return maxIdle;
    }
    
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    
    public int getMaxWait() {
        return maxWait;
    }
    
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}