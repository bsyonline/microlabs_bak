package com.rolex.microlabs.a.b.c;

public class TestI {
    public static void main(String[] args) {
        TestH test = new TestH();
        System.out.println(test.getNext());
        TestH testObject = new TestH();
        System.out.println(testObject.getNext());
        System.out.println(test.getNext());
    }
}