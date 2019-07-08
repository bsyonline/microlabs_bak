/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author rolex
 * @since 2018
 */
public interface DistributedLock {

    void unlock() throws LockingException;

    boolean tryLock(long timeout, TimeUnit unit) throws LockingException;

    void lock() throws LockingException;

    public class LockingException extends RuntimeException {
        public LockingException(String msg, Exception e) {
            super(msg, e);
        }

        public LockingException(String msg) {
            super(msg);
        }
    }
}
