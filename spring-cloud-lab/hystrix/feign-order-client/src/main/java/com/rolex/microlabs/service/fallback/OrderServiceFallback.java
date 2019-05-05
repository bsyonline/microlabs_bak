/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service.fallback;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.OrderClient;
import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2019
 */
@Component
public class OrderServiceFallback implements OrderClient {
    @Override
    public Order findById(String id) {
        return new Order("0", "", "0");
    }
}
