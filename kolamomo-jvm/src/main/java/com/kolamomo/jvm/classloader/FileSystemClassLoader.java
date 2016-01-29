package com.kolamomo.jvm.classloader;

import java.io.*;

/**
 * 自定义classLoader
 * 1. 继承java.lang.ClassLoader
 * 2. 重写findClass()方法
 */
public class FileSystemClassLoader extends ClassLoader {
    private String rootDir;

    public FileSystemClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //读取class文件，将内容保存在字节数组中
        byte[] classData = getClassData(name);
        if(classData == null) {
            throw new ClassNotFoundException();
        } else {
            //调用classloader的defineClass()方法，将字节码解析为class类型
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getClassData(String className) {
        String path = classNameToPath(className);
        try{
            InputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int byteNumRead = 0;
            while((byteNumRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, byteNumRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className) {
        return rootDir + File.separator + className.replace(".", File.separator) + ".class";
    }

    public static void main(String[] args) throws ClassNotFoundException {
        String rootDir = "/home/jay/project/kolamomo/kolamomo/kolamomo-jvm/target/classes";
        FileSystemClassLoader fileSystemClassLoader = new FileSystemClassLoader(rootDir);
        Class fsclClass = fileSystemClassLoader.findClass("com.kolamomo.jvm.classloader.FileSystemClassLoader");
        if(fsclClass==null) {
            System.out.println("class load fail");
        } else {
            System.out.println("class load success");
            //测试自定义classloader加载的class与系统加载器加载的class是否一致
            System.out.println(fsclClass == FileSystemClassLoader.class);
        }
    }
}
