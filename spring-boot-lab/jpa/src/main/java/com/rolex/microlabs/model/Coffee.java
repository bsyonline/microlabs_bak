/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author rolex
 * @since 2019
 */
@Entity
@Data
@Table(name = "jpa_coffee")
@NoArgsConstructor
@AllArgsConstructor
public class Coffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String coffee;
    float unitPrice;

    public Coffee(String coffee, float unitPrice) {
        this.coffee = coffee;
        this.unitPrice = unitPrice;
    }
}
