/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2019
 */
@Component
public class GZipCompress implements Compress {
    @Override
    public String type() {
        return "zip";
    }

    @Override
    public String compress(String path) {
        System.out.println("gzip compress");
        return null;
    }
}
