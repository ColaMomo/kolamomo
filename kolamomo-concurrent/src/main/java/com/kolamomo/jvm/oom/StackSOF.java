package com.kolamomo.jvm.oom;

/**
 * 栈空间OOM测试程序
 * VM args: -Xss160k
 */
public class StackSOF {
    private static int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        StackSOF stackSOF = new StackSOF();
        try{
            stackSOF.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length = " + stackLength);
            throw e;
        }
    }
}
