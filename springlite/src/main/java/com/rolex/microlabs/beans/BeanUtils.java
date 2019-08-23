/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.beans;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class BeanUtils {
    public static Map<String, MethodAccess> map = new HashMap();
    public static Map<String, Class> classMap = new HashMap();

    public BeanUtils() {
    }

    public static void setProperty(Object bean, String property, Object value) {
        String key = bean.getClass().getName() + "." + property;
        MethodAccess access = (MethodAccess)map.get(bean.getClass().getName() + "." + property);
        if(access == null) {
            access = MethodAccess.get(bean.getClass());
            map.put(key, access);
        }

        String methodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        access.invoke(bean, methodName, new Object[]{value});
    }

    public static Optional<Object> getProperty(Object bean, String property) {
        String key = bean.getClass().getName() + "." + property;
        MethodAccess access = (MethodAccess)map.get(property);
        if(access == null) {
            access = MethodAccess.get(bean.getClass());
            map.put(key, access);
        }

        String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        return Optional.fromNullable(access.invoke(bean, methodName, new Object[0]));
    }

    public static Optional<Object> newInstance(String name) throws ClassNotFoundException {
        Class clazz = (Class)loadClass(name).orNull();
        ConstructorAccess access = ConstructorAccess.get(clazz);
        return Optional.fromNullable(access.newInstance());
    }

    public static List<String> getFields(Class clazz) throws ClassNotFoundException {
        Field[] fields = clazz.getDeclaredFields();
        ArrayList list = new ArrayList();
        Field[] arr$ = fields;
        int len$ = fields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            list.add(field.getName());
        }

        return list;
    }

    public static List<String> getFields(String key, Class clazz) throws ClassNotFoundException {
        Field[] fields = clazz.getDeclaredFields();
        ArrayList list = new ArrayList();
        Field[] arr$ = fields;
        int len$ = fields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            list.add(key + "." + field.getName());
        }

        return list;
    }

    public static Optional<Class> loadClass(String name) throws ClassNotFoundException {
        Class clazz = Class.forName(name);
        return Optional.fromNullable(clazz);
    }

    public static List<Annotation> getAnnotations(Class clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        return Arrays.asList(annotations);
    }
}