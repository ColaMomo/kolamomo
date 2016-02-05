package com.kolamomo.io.nio.buffer;

/**
 * Created by jiangchao on 16/1/31.
 */
public class Hello {
    private String name = "lalala";

    public void hello() {
        System.out.println("hello, " + name);
    }

    public static void main(String[] args) {
        Hello hello = new Hello();
        hello.hello();
    }
}
