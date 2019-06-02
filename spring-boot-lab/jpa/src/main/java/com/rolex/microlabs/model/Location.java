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
@Table(name = "jpa_location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    float lng;
    float lat;
    @OneToOne
    @JoinColumn(name = "contact_id")
    Contact contact;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
