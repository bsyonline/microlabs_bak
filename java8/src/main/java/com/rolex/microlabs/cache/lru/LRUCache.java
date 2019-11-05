package com.rolex.microlabs.cache.lru;

import java.util.LinkedHashMap;

/**
 * @author rolex
 * @since 2019
 */
public class LRUCache extends AbstractCache {

    LinkedHashMap map;

    public LRUCache() {
        this(10);
    }

    public LRUCache(int capacity) {
        map = new LinkedHashMap(capacity, 1, true);
    }

    @Override
    public void put(String key, Object val) {
        map.put(key, val);
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }
}
