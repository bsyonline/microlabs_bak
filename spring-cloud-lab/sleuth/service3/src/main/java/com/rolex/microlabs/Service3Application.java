/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootApplication
@Slf4j
@RestController
public class Service3Application {
    public static void main(String[] args) {
        SpringApplication.run(Service3Application.class, args);
    }

    @GetMapping("/service3")
    public String service3() {
        log.info("calling trace {}", "Service3Application::service3");
        return "service3";
    }
}
