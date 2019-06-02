/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@Data
@Entity
@Table(name = "jpa_order")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer customerId;
    Integer userId;
    String name;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    List<Coffee> coffees;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Contact contact;
    @CreatedDate
    LocalDateTime idt;
    @LastModifiedDate
    LocalDateTime udt;
    @LastModifiedDate
    Long updateTime;
    @CreatedDate
    Date createTime;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", coffees=" + coffees +
                ", contact=" + contact +
                ", idt=" + idt +
                ", udt=" + udt +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
