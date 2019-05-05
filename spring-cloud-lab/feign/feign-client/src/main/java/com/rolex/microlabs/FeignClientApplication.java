/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rolex
 * @since 2019
 */
@EnableFeignClients
@SpringBootApplication
@RestController
public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }

    @Autowired
    OrderService orderService;

    @GetMapping("/orders/{id}")
    public Order findById(@PathVariable("id") String id){
        return orderService.findById(id);
    }
}
