package com.rolex.microlabs.a.b.c;

/**
 * @author rolex
 * @Since 06/11/2019
 */
public class TestF {
    public static void main(String[] args) {
        TestE test = new TestE();
        TestE testB = new TestE();
        System.out.println(test.equals(testB));
        System.out.println(test.name.equals(testB.name));
    }
}
