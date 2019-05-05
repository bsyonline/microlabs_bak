/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service.fallback;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.OrderClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class OrderServiceFallbackFactory implements FallbackFactory<OrderClient> {
    private static final Order order = new Order("0", "", "0");
    @Override
    public OrderClient create(Throwable throwable) {
        return new OrderClient() {
            @Override
            public Order findById(String id) {
                log.error("fallback findById", throwable);
                return order;
            }
        };
    }
}
