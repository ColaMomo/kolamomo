package com.kolamomo.concurrent.safety;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jay on 16-1-29.
 */
public class AtomicObject implements Runnable {
    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void run() {
        count.getAndIncrement();
    }
}
