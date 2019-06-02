/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.keep;

/**
 * @author rolex
 * @since 2018
 */
public class World {
    
    String name;
    
    public String world(){
        name = "hello world";
        return name.substring(1, 5);
    }
    
}
