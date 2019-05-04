/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs;

import com.google.common.collect.Lists;
import com.rolex.microlabs.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rolex
 * @since 2018
 */
@RefreshScope
@SpringBootApplication
@RestController
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Autowired
    HttpServletRequest request;
    @Value("${msg}")
    String msg;

    @GetMapping("/msg")
    String getMsg() {
        return msg;
    }

    @GetMapping("/orders")
    public List<Order> list() {
        return Lists.newArrayList(new Order("1", LocalDateTime.now().toString(), "1"), new Order("2", LocalDateTime.now().toString(), "2"));
    }

    @PostMapping("/orders")
    public Order add(@RequestBody Order order) {
        return order;
    }

    @GetMapping("/orders/{id}")
    public Order getById(@PathVariable String id) {
        return new Order("1", LocalDateTime.now().toString(), "1");
    }

    @GetMapping("/orders/types")
    public List<Order> getByType(@RequestParam("type") String type) {
        return Lists.newArrayList(new Order("1", LocalDateTime.now().toString(), "1"));
    }

    @GetMapping("/types/{type}")
    public List<Order> getByType2(@PathVariable String type) {
        return Lists.newArrayList(new Order("2", LocalDateTime.now().toString(), "2"));
    }

    @GetMapping("/types/{id}")
    public String getByType3(@PathVariable String typeId) {
        return "/types/{id}";
    }

    @GetMapping("/hello")
    public String hello() {
        return request.getRequestURI();
    }

    @GetMapping("/world")
    public String world() {
        return request.getRequestURI();
    }

    @GetMapping("/helloworld")
    public String helloworld() {
        return request.getRequestURI();
    }

    @GetMapping("/hello/world")
    public String helloworld1() {
        return request.getRequestURI();
    }

    @GetMapping("/hello/{id}/world")
    public String helloworld2(@PathVariable("id") String id) {
        return request.getRequestURI();
    }

    @GetMapping("/hello/{id}/cloud/world")
    public String helloworld3(@PathVariable("id") String id) {
        return request.getRequestURI();
    }

    @GetMapping("/{id}/cloud/world")
    public String helloworld03(@PathVariable("id") String id) {
        return request.getRequestURI();
    }

    @GetMapping("/{id}/world")
    public String helloworld4(@PathVariable("id") String id) {
        return request.getRequestURI();
    }

    @GetMapping("/{id}/world/cloud")
    public String helloworld5(@PathVariable("id") String id) {
        return request.getRequestURI();
    }

    @GetMapping("/cloud")
    public String cloud() {
        return request.getRequestURI();
    }

    @GetMapping("/spring/cloud")
    public String cloud1() {
        return request.getRequestURI();
    }

}
