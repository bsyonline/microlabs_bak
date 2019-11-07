package com.rolex.microlabs.a.b.c.d;

import com.rolex.microlabs.a.b.c.Test;

public class TestB {
    private String name;
    private String desc;
    
    public TestB() {
        Test test = new Test();
//        name = test.name; // 编译不过
//        desc = test.desc; // 编译不过
    }
}