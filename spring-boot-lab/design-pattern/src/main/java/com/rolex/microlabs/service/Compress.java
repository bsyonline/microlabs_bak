package com.rolex.microlabs.service;

/**
 * @author rolex
 * @since 2019
 */
public interface Compress {
    String type();

    String compress(String path);
}
