package com.kolamomo.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存OOM测试程序
 * VM Args: -Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {
    static class OOMObject {}

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();

        while(true) {
            list.add(new OOMObject());
        }
    }
}

//输出结果：
/*
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid2826.hprof ...
Heap dump file created [13093348 bytes in 0.062 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
    at java.util.Arrays.copyOf(Arrays.java:3210)
    at java.util.Arrays.copyOf(Arrays.java:3181)
    at java.util.ArrayList.grow(ArrayList.java:261)
    at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:235)
    at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:227)
    at java.util.ArrayList.add(ArrayList.java:458)
    at com.kolamomo.jvm.oom.HeapOOM.main(HeapOOM.java:16)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:497)
    at com.intellij.rt.execution.application.AppMain.main(AppMain.java:140)

抛出oom异常后，会将堆内存转存到工程目录下的java_pid2826.hprof文件中。
可以使用jvisulvm查看文件。

*/