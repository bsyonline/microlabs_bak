package com.rolex.microlabs.service;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.fallback.OrderServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author rolex
 * @since 2019
 */
@FeignClient(name = "order-service", fallbackFactory = OrderServiceFallbackFactory.class)
//@FeignClient(name = "order-service", fallback = OrderServiceFallback.class)
@Service
public interface OrderClient {

    @GetMapping(value = "/orders/{id}")
    Order findById(@PathVariable("id") String id);

}