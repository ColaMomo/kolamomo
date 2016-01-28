package com.kolamomo.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量池OOM测试程序
 * VM args: -XX:PermSize=10M -XX:MaxPermSize=10M （jdk 6）
 * java 7 intern() 返回的结果存放在普通堆中
 */
public class ConstantPoolOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while(true) {
            list.add(String.valueOf(i++).intern());
        }
    }
}
