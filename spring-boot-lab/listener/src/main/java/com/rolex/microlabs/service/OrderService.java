/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.event.OrderCreatedEvent;
import com.rolex.microlabs.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author rolex
 * @since 2019
 */
@Service
public class OrderService {
    
    @Autowired
    ApplicationContext context;
    
    public void add() {
        // 1. create order
        Order order = new Order(1, 1, LocalDateTime.now());
        System.out.println("create order");
        context.publishEvent(new OrderCreatedEvent(order));
    }
}
