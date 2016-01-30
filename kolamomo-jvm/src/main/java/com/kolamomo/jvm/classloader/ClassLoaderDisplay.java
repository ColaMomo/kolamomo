package com.kolamomo.jvm.classloader;

/**
 * ClassLoader相关属性
 */
public class ClassLoaderDisplay {
    public static void main(String[] args) {
        //classLoader的继承体系
        String rootDir = "/home/jay/project/kolamomo/kolamomo/kolamomo-jvm/target/classes";
        ClassLoader classLoader = new FileSystemClassLoader(rootDir);
        while(classLoader != null) {
            System.out.println(classLoader);
            classLoader = classLoader.getParent();
        }

        //classLoader的可见性
        try {
            Class.forName("com.kolamomo.jvm.classloader.ClassLoaderDisplay", true,
                    ClassLoaderDisplay.class.getClassLoader().getParent());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
