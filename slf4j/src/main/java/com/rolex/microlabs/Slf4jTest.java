/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rolex
 * @since 2019
 */
public class Slf4jTest {

    static Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
    public static void main(String[] args) {
        logger.info("today is {}, the air temperature is {} at {}.", "Warmer Day", "18 degrees", "10:00");
    }

}
