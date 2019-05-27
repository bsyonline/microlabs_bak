/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * @author rolex
 * @since 2019
 */
public class MyLoggerFactory implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        return new MyLogger();
    }
}
