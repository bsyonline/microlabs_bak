/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.event.OrderCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author rolex
 * @since 2019
 */
@Service
public class MailService implements ApplicationListener<OrderCreatedEvent> {

    @Override
    public void onApplicationEvent(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("send mail: " + orderCreatedEvent.getSource());
    }
}
