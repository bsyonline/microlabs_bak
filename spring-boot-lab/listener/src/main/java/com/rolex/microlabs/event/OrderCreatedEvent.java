/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.event;

import com.rolex.microlabs.model.Order;
import org.springframework.context.ApplicationEvent;

/**
 * @author rolex
 * @since 2019
 */
public class OrderCreatedEvent extends ApplicationEvent {
    public OrderCreatedEvent(Order order) {
        super(order);
    }
}
