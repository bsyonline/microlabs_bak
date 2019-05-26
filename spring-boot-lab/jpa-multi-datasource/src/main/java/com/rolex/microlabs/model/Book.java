/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author rolex
 * @since 2018
 */
@Entity
@Table(name="t_book")
@Data
@ToString
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="customer_id")
    private Integer customerId;
    private String name;
    private Double price;

    protected Book() {
    }

    public Book(String name, Double price, Integer customerId) {
        this.name = name;
        this.price = price;
        this.customerId = customerId;
    }

}