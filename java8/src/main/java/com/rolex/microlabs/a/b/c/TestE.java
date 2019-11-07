package com.rolex.microlabs.a.b.c;

/**
 * @author rolex
 * @Since 06/11/2019
 */
public class TestE {
    public String name = "hello";
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
