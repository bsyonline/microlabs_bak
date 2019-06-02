/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.provider;

import org.drools.template.DataProvider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author rolex
 * @since 2018
 */
public class ProductProvider implements DataProvider {
    private Iterator<Map<String, Object>> iterator;
    
    public ProductProvider(List<Map<String, Object>> rows) {
        this.iterator = rows.iterator();
    }
    
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    
    @Override
    public String[] next() {
        Map<String, Object> nextRule = iterator.next();
        return new String[]{
            (String) nextRule.get("type"),
            String.valueOf(nextRule.get("discount"))
        };
    }
}
