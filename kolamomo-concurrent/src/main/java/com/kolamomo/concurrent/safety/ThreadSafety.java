package com.kolamomo.concurrent.safety;

/**
 * 线程安全测试类
 */
public class ThreadSafety {
    public void runSafeGernerator() {
        SafeGernerator safeGernerator = new SafeGernerator();
        for(int i = 0; i <= 10; i++) {
            new Thread(safeGernerator).start();
        }
    }


    public void runUnsafeGernerator() {
        UnsafeGernerator unsafeGernerator = new UnsafeGernerator();
        for(int i = 0; i <= 10; i++) {
            new Thread(unsafeGernerator).start();
        }
    }

    /**
     * 线程不安全的value生成器
     */
    public class UnsafeGernerator implements Runnable {
        private int value = 0;

        public int getNext() {
            return value++;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(getNext() + " ");
        }
    }

    /**
     * 线程不安全的value生成器
     * 对++操作进行加锁
     */
    public class SafeGernerator implements Runnable {
        private volatile int value = 0;

        public synchronized int getNext() {
            return value++;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(getNext() + " ");
        }
    }

    public static void main(String[] args) {
        ThreadSafety threadSafety = new ThreadSafety();
        System.out.println("unsafe gernerator: ");
        threadSafety.runUnsafeGernerator();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("safe gernerator: ");
        threadSafety.runSafeGernerator();
    }
}
/*
output:

unsafe gernerator:
0 1 0 2 3 4 5 6 7 8 9
safe gernerator:
0 1 2 3 4 5 6 7 8 9 10
*/
