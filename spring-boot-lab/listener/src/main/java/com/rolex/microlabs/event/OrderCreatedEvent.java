/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * @author rolex
 * @since 2019
 */
public class OrderCreatedEvent extends ApplicationEvent {
    public OrderCreatedEvent(ApplicationContext context) {
        super(context);
    }
}
