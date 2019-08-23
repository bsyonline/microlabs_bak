/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.factory;

import com.rolex.microlabs.beans.GenericProcessor;

/**
 * @author rolex
 * @since 2019
 */
public class ApplicationContext extends BeanFactory {
    
    public static void run(Class clazz, String... args) {
        try {
            BeanFactory factory = new BeanFactory().register(clazz);
            GenericProcessor processor = (GenericProcessor) factory.getBean(args[0]);
            processor.service();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
