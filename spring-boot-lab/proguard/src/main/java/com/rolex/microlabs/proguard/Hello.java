/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.proguard;

/**
 * @author rolex
 * @since 2018
 */
public class Hello {
    
    String password;
    
    public String hello(){
        password = "hello world";
        return password.substring(1, 5);
    }
}
