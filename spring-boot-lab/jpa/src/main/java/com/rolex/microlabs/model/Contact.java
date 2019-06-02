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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jpa_contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String phone;
    String email;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Location location;
    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", location=" + location +
                '}';
    }
}
