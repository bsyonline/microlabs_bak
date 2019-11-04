package com.rolex.microlabs.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author rolex
 * @Since 29/10/2019
 */
public class MyClassLoader extends URLClassLoader {

    String basePath;

    public MyClassLoader(URL[] urls) {
        super(urls);
        this.basePath = urls[0].getPath();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = this.findLoadedClass(name);
        if (clazz == null) {
            byte[] data = new byte[0];
            try {
                data = getClassData(basePath, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (data == null) {
                throw new RuntimeException();
            }
            clazz = defineClass(name, data, 0, data.length);
        }
        return clazz;
    }

    private byte[] getClassData(String path, String name) throws IOException {
        if (path.endsWith(".jar")) {
            return getClassDataFromJarFile(path, name);
        } else {
            return getClassDataFromFile(path, name);
        }
    }

    private byte[] getClassDataFromFile(String path, String packageName) throws IOException {
        String className = "file://" + path + packageName.replace(".", "/") + ".class";
        return getBytes(className);
    }

    private byte[] getBytes(String className) throws IOException {
        InputStream is;
        URL url = new URL(className);
        byte[] buf = new byte[1024];
        is = url.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int length = 0;
        while ((length = is.read(buf)) != -1) {
            bos.write(buf, 0, length);
        }
        byte[] bytes = bos.toByteArray();
        bos.flush();
        bos.close();
        is.close();
        return bytes;
    }

    public byte[] getClassDataFromJarFile(String path, String name) throws IOException {
        JarInputStream jis = new JarInputStream(new FileInputStream(path));
        JarEntry jarEntry = jis.getNextJarEntry();
        while (jarEntry != null) {
            String fileName = jarEntry.getName();
            if (fileName.endsWith(name.replace(".", "/") + ".class")) {
                jarEntry = null;
                String fullPath = "jar:file:" + path + "!/" + fileName;
                return getBytes(fullPath);
            } else {
                jarEntry = jis.getNextJarEntry();
            }
        }
        return null;
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String path = "file://D:/Dev/IdeaProjects/microlabs/java8/data/";

        String name = "com.rolex.microlabs.classloader.Counter";
        MyClassLoader classLoader = new MyClassLoader(new URL[]{new URL(path)});
        Class clazz = classLoader.loadClass(name);
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("add");
        method.invoke(obj, new Object[]{});

        String path1 = "file://D:/Dev/IdeaProjects/microlabs/java8/data/counter.jar";
        String name1 = "com.rolex.microlabs.classloader.Counter";
        MyClassLoader classLoader1 = new MyClassLoader(new URL[]{new URL(path1)});
        Class clazz1 = classLoader1.loadClass(name1);
        Object obj1 = clazz1.newInstance();
        Method method1 = clazz1.getDeclaredMethod("add");
        method1.invoke(obj1, new Object[]{});

    }
}