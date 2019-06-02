/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author rolex
 * @since 2019
 */
public class LambdaTest {

    public static void main(String[] args) {
        //创建stream
        List list = new ArrayList<>();
        list.stream();
        list.parallelStream();
        Arrays.stream(new String[]{"a", "b", "c"});

        //distinct
        System.out.println(Arrays.stream(new String[]{"a", "b", "c", "a", "b", "c"})
                .distinct().collect(Collectors.toList()));

        //filter
        System.out.println(Arrays.stream(new String[]{"a", "b", "c", "a", "b", "c"})
                .filter(s -> s.equals("a")).collect(Collectors.toList()));

        //map
        System.out.println(Arrays.stream(new User[]{
                new User("bob", 1, 19),
                new User("alice", 2, 20),
                new User("jim", 1, 18),
                new User("jan", 2, 25),
                new User("david", 1, 23),
                new User("lucy", 2, 17)})
                .map(User::getName).collect(Collectors.toList()));

        //mapToInt
        System.out.println(Arrays.stream(new User[]{
                new User("bob", 1, 19),
                new User("alice", 2, 20),
                new User("jim", 1, 18),
                new User("jan", 2, 25),
                new User("david", 1, 23),
                new User("lucy", 2, 17)})
                .mapToInt(User::getAge).max().getAsInt());

        //flatMap
        System.out.println(Stream.of(new String[]{"a", "b"}, new String[]{"1", "2"}, new String[]{"x", "y", "z"})
                .flatMap(s -> Arrays.stream(s)).collect(Collectors.toList()));

        //peek
        System.out.println(Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        ).peek(e -> System.out.println(e.size())) // 查看每个元素(list)的 size
                .collect(Collectors.toList()));

        //limit
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7).limit(4).collect(Collectors.toList()));

        //skip
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7).skip(4).collect(Collectors.toList()));

        //reduce
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7).reduce(0, (x, y) -> x + y));
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7).reduce(0, Integer::sum));
        System.out.println(Stream.of("1", "2", "3", "4", "5", "6", "7").reduce("", String::concat));

        //collect
        System.out.println(Stream.of("a", "b", "c").collect(
                () -> new ArrayList<>(),
                (c, e) -> c.add(e.toUpperCase()),
                (c1, c2) -> c1.addAll(c2)
        ));
        System.out.println(Stream.of("a", "b", "c").map(String::toUpperCase).collect(Collectors.toList()));

        //group
        System.out.println(Arrays.stream(new User[]{
                new User("bob", 1, 19),
                new User("alice", 2, 20),
                new User("jim", 1, 18),
                new User("jan", 2, 25),
                new User("david", 1, 23),
                new User("lucy", 2, 17)})
                .collect(Collectors.groupingBy(User::getGender)));
    }
}


class User {
    String name;
    int gender;
    int age;

    public User(String name, int gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}