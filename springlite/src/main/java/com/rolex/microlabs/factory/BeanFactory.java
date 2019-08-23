/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.factory;

import com.rolex.microlabs.beans.BeanUtils;
import com.rolex.microlabs.stereotype.Autowired;
import com.rolex.microlabs.stereotype.Component;
import com.rolex.microlabs.stereotype.ComponentScan;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class BeanFactory {
    
    private List<String> classNames = new ArrayList<>();
    private List<String> basePackages = new ArrayList<>();
    private Map<String, Object> registryMap = new HashMap<>();
    private Map<Class, Object> beanMap = new HashMap<>();
    
    public BeanFactory register(Class... annotatedClasses) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (annotatedClasses.length == 0) {
            System.out.println("At least one annotated class must be specified");
        }
        for (Class clazz : annotatedClasses) {
            for (Annotation annotation : BeanUtils.getAnnotations(clazz)) {
                if (annotation instanceof ComponentScan) {
                    ComponentScan componentScan = (ComponentScan) annotation;
                    basePackages.addAll(Arrays.asList(componentScan.basePackages()));
                }
            }
        }
        
        if (basePackages.size() == 0) {
            log.info("basePackages must be specified");
        }
        for (String packageName : basePackages) {
            List<String> list = new BeanFactory().doScan(packageName, classNames, false);
            for (String name : list) {
                Class clazz = BeanUtils.loadClass(name).orNull();
                for (Annotation annotation : BeanUtils.getAnnotations(clazz)) {
                    if (annotation instanceof Component) {
                        Object obj = BeanUtils.newInstance(name).orNull();
                        registryMap.put(getComponentName(clazz), obj);
                    }
                }
            }
        }
        autowire();
        return this;
    }
    
    public Map<String, Object> getRegistryMap() {
        return registryMap;
    }
    
    public <T> T getBean(Class<T> clazz) {
        return (T) beanMap.get(clazz);
    }
    
    public Object getBean(String name) {
        return registryMap.get(name);
    }
    
    public String getComponentName(Class clazz) {
        String componentName = null;
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Component) {
                componentName = ((Component) annotation).value();
            }
        }
        return "".equals(componentName) ? clazz.getName() : componentName;
    }
    
    public void autowire() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        for (Map.Entry<String, Object> kv : registryMap.entrySet()) {
            Class clazz = kv.getValue().getClass();
            for (Field field : getAllFields(clazz, new ArrayList<Field>())) {
                Autowired annotation = field.getAnnotation(Autowired.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Object clazzObj = getRegistryMap().get(kv.getKey());
                    Object fieldClazzObj = getRegistryMap().get(field.getType().getName());
                    field.set(clazzObj, fieldClazzObj);
                }
            }
        }
    }
    
    public List<Field> getAllFields(Class clazz, List<Field> allFields) {
        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            getAllFields(clazz.getSuperclass(), allFields);
        }
        return allFields;
    }
    
    public List<String> doScan(String basePackage, List<String> nameList, boolean includeInnerClass) throws IOException {
        ClassLoader cl = BeanFactory.class.getClassLoader();
        String splashPath = dotToSplash(basePackage);
        
        URL url = cl.getResource(splashPath);
        if (url != null) {
            String filePath = getRootPath(url);
            List<String> names;
            if (isJarFile(filePath)) {
                names = readFromJarFile(filePath, splashPath);
            } else {
                names = readFromDirectory(filePath);
            }
            
            for (String name : names) {
                if (isClassFile(name)) {
                    if (includeInnerClass) {
                        nameList.add(toFullyQualifiedName(name, basePackage));
                    } else {
                        if (!isInnerClassFile(name)) {
                            nameList.add(toFullyQualifiedName(name, basePackage));
                        }
                    }
                } else {
                    doScan(basePackage + "." + name, nameList, includeInnerClass);
                }
            }
        }
        return nameList;
    }
    
    public static String trimExtension(String name) {
        int pos = name.lastIndexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }
        return name;
    }
    
    private static String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(trimExtension(shortName));
        
        return sb.toString();
    }
    
    private static List readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();
        
        if (null == names) {
            return null;
        }
        
        return Arrays.asList(names);
    }
    
    private static List readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();
        
        List<String> nameList = new ArrayList();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                name = name.replace("/", ".").substring(splashedPackageName.length() + 1, name.length());
                nameList.add(name);
            }
            entry = jarIn.getNextJarEntry();
        }
        
        return nameList;
    }
    
    private static boolean isClassFile(String name) {
        return name.endsWith(".class");
    }
    
    private static boolean isInnerClassFile(String name) {
        return name.contains("$");
    }
    
    private static boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }
    
    public static String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');
        
        if (-1 == pos) {
            return fileUrl;
        }
        
        return fileUrl.substring(5, pos);
    }
    
    public static String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }
}
