package com.rolex.microlabs.a.b.c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author rolex
 * @Since 07/11/2019
 */
public class TestM {
    
    Map<String, Object> datas = new ConcurrentHashMap<String, Object>();
    
    Object lock = new Object();
    
    ReentrantLock reentrantLock = new ReentrantLock();
    
    public Object get(String keyName) {
        Object data = null;
//        synchronized (lock) {
        reentrantLock.lock();
        try {
            if (!datas.containsKey(keyName)) {
                data = createData(keyName);
                datas.put(keyName, data);
            } else {
                data = datas.get(keyName);
            }
        }finally {
            reentrantLock.unlock();
        }
//        }
        return data;
    }
    
    private Object createData(String keyName) {
        return new Object();
    }
    
}
