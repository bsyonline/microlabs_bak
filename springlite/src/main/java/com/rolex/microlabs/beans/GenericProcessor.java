/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.beans;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public abstract class GenericProcessor implements Processor {
    
    public void service() {
        try {
            init();
            process();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            destroy();
        }
    }
}
