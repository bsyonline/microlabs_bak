/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.dao;

import com.rolex.microlabs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rolex
 * @since 2019
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
