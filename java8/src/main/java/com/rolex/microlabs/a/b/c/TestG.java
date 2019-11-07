package com.rolex.microlabs.a.b.c;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestG {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("a");
        Set set = new HashSet();
        set.add("a");
        set.add("b");
        set.add("a");
        System.out.println(list.size());
        System.out.println(set.size());
    }
}