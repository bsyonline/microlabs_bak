package com.rolex.microlabs;

/**
 * @author rolex
 * @since 2019
 */
public interface Cache {

    public void put(String key, Object val);

    public Object get(String key);

    int size();

    void remove();

}
