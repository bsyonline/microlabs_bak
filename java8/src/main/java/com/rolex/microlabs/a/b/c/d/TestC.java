package com.rolex.microlabs.a.b.c.d;

import com.rolex.microlabs.a.b.c.Test;

public class TestC extends Test {
    private String cname;
    private String cdesc;
    
    public TestC() {
//        cname = name; // 编译不过
        cdesc = desc;
    }
}