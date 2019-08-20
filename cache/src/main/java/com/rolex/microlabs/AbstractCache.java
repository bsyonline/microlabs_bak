package com.rolex.microlabs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author rolex
 * @since 2019
 */
public abstract class AbstractCache implements Cache {

    int capacity;
    HashMap map = new LinkedHashMap(capacity, 1, true);

    @Override
    public abstract void put(String key, Object val);

    @Override
    public abstract Object get(String key);

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void remove() {

    }
}
