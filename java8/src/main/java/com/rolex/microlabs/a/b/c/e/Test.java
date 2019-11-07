package com.rolex.microlabs.a.b.c.e;

public class Test {
    public void call(int state) throws IllegalStateException {
        if (state == 0) {
              throw new IllegalStateException("state is 0");
        }
    }
    
    public void invoke(int state) throws TestException {
        if (state == 0) {
            throw new TestException("state is 0");
        }
    }
}