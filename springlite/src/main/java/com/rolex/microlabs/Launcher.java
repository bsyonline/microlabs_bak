/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.factory.ApplicationContext;
import com.rolex.microlabs.stereotype.ComponentScan;

/**
 * @author rolex
 * @since 2019
 */
@ComponentScan(basePackages = "com.rolex.microlabs.service")
public class Launcher {
    public static void main(String[] args) {
        ApplicationContext.run(Launcher.class, args);
    }
}
