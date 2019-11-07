package com.rolex.microlabs.a.b.c;

/**
 * @author rolex
 * @Since 06/11/2019
 */
public class TestD {
    public static void main(String[] args) {
        String a = new String("a");
        String b = "b";
        String c = a + b;
        
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("a");
        strBuf.append("b");
        String d = strBuf.toString();
        
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("a");
        strBuilder.append("b");
        String e = strBuilder.toString();
    }
}
