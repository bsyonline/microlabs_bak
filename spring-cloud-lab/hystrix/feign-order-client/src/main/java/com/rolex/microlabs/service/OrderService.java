package com.rolex.microlabs.service;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.fallback.OrderServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author rolex
 * @since 2019
 */
@FeignClient(name = "order-service", fallback = OrderServiceFallback.class)
@Component
public interface OrderService {

    @GetMapping(value = "/orders/{id}")
    Order findById(@PathVariable("id") String id);

}