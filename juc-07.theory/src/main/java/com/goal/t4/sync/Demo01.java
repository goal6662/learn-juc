package com.goal.t4.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * 轻量级锁
 */
@Slf4j(topic = "Sync")
public class Demo01 {

    public static final Object lock = new Object();

    public static void method1() {
        synchronized (lock) {
            // 同步块A
            method2();
        }
    }

    public static void method2() {
        synchronized (lock) {
            // 同步块B
        }
    }

    public static void main(String[] args) {

    }

}
