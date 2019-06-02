package com.rolex.microlabs.dao;

import com.rolex.microlabs.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rolex
 * @since 2019
 */
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
}
