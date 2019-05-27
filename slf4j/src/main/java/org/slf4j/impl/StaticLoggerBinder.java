/*
 * Copyright (C) 2019 bsyonline
 */
package org.slf4j.impl;

import com.rolex.microlabs.MyLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * @author rolex
 * @since 2019
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

    static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    ILoggerFactory loggerFactory = new MyLoggerFactory();
    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactory.getClass().getName();
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }
}
