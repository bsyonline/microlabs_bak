/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.config;

import com.netflix.loadbalancer.IRule;
import com.rolex.microlabs.rule.MyRibbonRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rolex
 * @since 2019
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new MyRibbonRule();
    }
}
