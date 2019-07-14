/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.event.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
        System.out.println("create order");
        context.publishEvent(new OrderCreatedEvent(context));
    }
}
