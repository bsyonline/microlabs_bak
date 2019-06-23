/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.consumer.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author rolex
 * @since 2019
 */
@Data
@ToString
public class Order {
    private Integer id;
    private Integer userId;
    private LocalDateTime idt;

    public Order(Integer id) {
        this.id = id;
    }
}
