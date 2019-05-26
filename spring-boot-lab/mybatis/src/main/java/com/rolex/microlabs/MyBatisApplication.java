/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author rolex
 * @since 2018
 */
@SpringBootApplication
@MapperScan("com.rolex.microlabs.dao")
public class MyBatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }
}
