package com.kolamomo.concurrent.safety;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全测试类
 */
public class ThreadSafety {
    public void runSafeSyncGernerator() {
        List<Thread> threadList = new ArrayList<Thread>();
        SafeSyncGernerator safeGernerator = new SafeSyncGernerator();
        for(int i = 0; i < 10000; i++) {
            Thread t = new Thread(safeGernerator);
            threadList.add(t);
        }

        long startTime = System.currentTimeMillis();
        for(Thread t : threadList) {
            t.start();
        }
        for(Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(safeGernerator.getValue());
        System.out.println("cost: " + (System.currentTimeMillis() - startTime));
    }

    public void runSafeAtomicGernerator() {
        List<Thread> threadList = new ArrayList<Thread>();
        SafeAtomicGernerator safeGernerator = new SafeAtomicGernerator();
        for(int i = 0; i < 10000; i++) {
            Thread t = new Thread(safeGernerator);
            threadList.add(t);
        }

        long startTime = System.currentTimeMillis();
        for(Thread t : threadList) {
            t.start();
        }
        for(Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(safeGernerator.getValue());
        System.out.println("cost: " + (System.currentTimeMillis() - startTime));
    }


    public void runUnsafeGernerator() {
        List<Thread> threadList = new ArrayList<Thread>();

        UnsafeGernerator unsafeGernerator = new UnsafeGernerator();
        for(int i = 0; i <= 10000; i++) {
            Thread t = new Thread(unsafeGernerator);
            threadList.add(t);
        }
        long startTime = System.currentTimeMillis();
        for(Thread t : threadList) {
            t.start();
        }
        for(Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(unsafeGernerator.getValue());
        System.out.println("cost: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 线程不安全的value生成器
     */
    public class UnsafeGernerator implements Runnable {
        private int value = 0;

        public void run() {
            for(int i = 0; i < 10000; i++) {
                value++;
            }
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 线程不安全的value生成器
     * 对++操作进行加锁
     */
    public class SafeSyncGernerator implements Runnable {
        private volatile int value = 0;

        public void run() {
            synchronized(this) {
                for(int i = 0; i < 10000; i++) {
                    value++;
                }
            }
        }

        public int getValue() {
            return value;
        }
    }

    public class SafeAtomicGernerator implements Runnable {
        private AtomicInteger value = new AtomicInteger(0);

        public void run() {
            for(int i = 0; i < 10000; i++) {
                value.getAndIncrement();
            }
        }

        public int getValue() {
            return value.get();
        }
    }


    public static void main(String[] args) {
        ThreadSafety threadSafety = new ThreadSafety();
        System.out.println("unsafe gernerator: ");
        threadSafety.runUnsafeGernerator();
        System.out.println("safe synchronized gernerator: ");
        threadSafety.runSafeSyncGernerator();
        System.out.println("safe atomic gernerator: ");
        threadSafety.runSafeAtomicGernerator();
    }
}
/*
output:

unsafe gernerator:
0 1 0 2 3 4 5 6 7 8 9
safe gernerator:
0 1 2 3 4 5 6 7 8 9 10
*/
