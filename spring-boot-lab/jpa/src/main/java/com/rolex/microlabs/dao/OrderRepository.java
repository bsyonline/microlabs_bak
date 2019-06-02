package com.rolex.microlabs.dao;

import com.rolex.microlabs.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rolex
 * @since 2019
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
