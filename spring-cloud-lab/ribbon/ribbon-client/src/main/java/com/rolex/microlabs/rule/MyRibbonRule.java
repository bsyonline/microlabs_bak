/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class MyRibbonRule extends AbstractLoadBalancerRule {
    private AtomicInteger nextServerCyclicCounter;
    private AtomicInteger times;

    public MyRibbonRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
        times = new AtomicInteger(0);
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();
            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            int nextServerIndex = incrementTimes();
            server = allServers.get(nextServerIndex);

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }
            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            // Next.
            server = null;
        }
        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }

    private int incrementTimes() {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = times.addAndGet(1);
            if (next < 10) {
                log.info("times = {}", times);
                return current;
            } else {
                times.set(0);
                log.info("times set 0 and current = {}", current);
                return current + 1;
            }
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}
