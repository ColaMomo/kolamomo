package com.kolamomo.jvm.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 直接内存OOM测试
 * VM Args: -Xmx20M -XX:MaxDirectMemorySize=10M
 */
public class DirectMemoryOOM {
    private static final int _10MB = 1024 * 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe)unsafeField.get(null);
        while(true) {
            unsafe.allocateMemory(_10MB);
        }
    }
}
