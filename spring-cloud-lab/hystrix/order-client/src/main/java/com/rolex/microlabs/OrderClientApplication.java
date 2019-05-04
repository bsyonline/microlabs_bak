/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.rolex.microlabs.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootApplication
@RestController
@EnableCircuitBreaker
@Slf4j
public class OrderClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderClientApplication.class, args);
    }

    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @HystrixCommand(fallbackMethod = "defaultOrder", commandProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "100"),
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "1")
    })
    @GetMapping("/orders/{id}")
    public Order findById(@PathVariable("id") String id) {
        return restTemplate.getForObject("http://order-service/orders/" + id, Order.class);
    }

    public Order defaultOrder(String id, Throwable e) {
        log.error("fallback findById", e);
        return new Order("0", "", "0");
    }
}
