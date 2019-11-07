package com.rolex.microlabs.classloader;

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
    
    private byte[] getClassData(String path, String packageName) throws IOException {
        
        if (path.endsWith(".jar")) {
            return getClassDataFromJarFile(path);
        } else {
            return getClassDataFromFile(path, packageName);
        }
        
    }
    
    private byte[] getClassDataFromFile(String path, String packageName) throws IOException {
        InputStream is = null;
        String className = "file://" + path + packageName.replace(".", "/") + ".class";
        URL url = new URL(className);
        byte[] buf = new byte[1024];
        is = url.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int length = 0;
        while ((length = is.read(buf)) != -1) {
            bos.write(buf, 0, length);
        }
        return bos.toByteArray();
    }
    
    public byte[] getClassDataFromJarFile(String path) throws IOException {
        JarInputStream jis = new JarInputStream(new FileInputStream(path));
        JarEntry jarEntry = jis.getNextJarEntry();
        while (jarEntry != null) {
            String name = jarEntry.getName();
            if (name.endsWith("Cat.class")) {
                System.out.println(name);
                jarEntry = null;
                
                URL url = new URL(name);
                InputStream is = url.openStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int length = 0;
                while ((length = is.read(buf)) != -1) {
                    bos.write(buf, 0, length);
                }
                return bos.toByteArray();
                
            } else {
                jarEntry = jis.getNextJarEntry();
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        String path = "file:///home/rolex";
        String path = "jar:file:/home/rolex/shako-1.0-SNAPSHOT.jar!/com/rolex/shako/bio/Cat.class";
        String name = "com.rolex.shako.bio.Cat";
        MyClassLoader classLoader = new MyClassLoader(new URL[]{new URL(path)});
        
        Class clazz = classLoader.loadClass(name);
        
        Object obj = clazz.newInstance();
        
        Method method = clazz.getDeclaredMethod("say");
        
        method.invoke(obj, new Object[]{});
        
    }
}