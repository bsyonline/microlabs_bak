/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.consumer.service.impl;

import com.rolex.microlabs.consumer.model.Order;
import com.rolex.microlabs.consumer.service.OrderService;
import com.rolex.microlabs.consumer.service.UserService;
import com.rolex.microlabs.provider.model.User;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Override
    public void save(Order order) {
        UserService userService = new UserServiceImpl();
        User user = userService.findById(1);
        log.info("user info : {}", user);
        order.setUserId(user.getId());
        order.setIdt(LocalDateTime.now());
        log.info("insert {} into database", order);
    }

    public static void main(String[] args) {
        Order order = new Order(1);
        new OrderServiceImpl().save(order);
    }
}
