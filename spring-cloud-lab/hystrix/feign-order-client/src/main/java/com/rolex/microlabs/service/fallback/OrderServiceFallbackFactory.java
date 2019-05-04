/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service.fallback;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.OrderService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class OrderServiceFallbackFactory implements FallbackFactory<OrderService> {
    @Override
    public OrderService create(Throwable throwable) {
        return new OrderService() {
            @Override
            public Order findById(String id) {
                log.error("fallback findById", throwable);
                return new Order("0", "", "0");
            }
        };
    }
}
