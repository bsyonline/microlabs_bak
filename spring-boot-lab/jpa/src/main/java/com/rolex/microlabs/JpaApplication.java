/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootApplication
@EnableJpaAuditing
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }
}
