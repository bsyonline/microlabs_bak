/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * @author rolex
 * @since 2018
 */
@Entity
@Table(name = "t_customer")
@Data
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @JsonIgnoreProperties(value = {"books"})
    @Transient
    private Set<Book> books;

    protected Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


}