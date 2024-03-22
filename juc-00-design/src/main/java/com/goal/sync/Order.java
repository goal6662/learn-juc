package com.goal.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * 顺序执行：先打印 2 后 打印 1
 * wait notify 版
 */
@Slf4j(topic = "order")
public class Order {

    private static final Object lock = new Object();
    private static int printed = 1;

    public static void main(String[] args) {

        Thread print2 = new Thread(() -> {
            // 获取到锁（打印资格
            synchronized (lock) {
                while (true) {
                    if (printed == 1) {
                        log.debug("2");
                        printed = 2;
                        // 唤醒另一个线程
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }, "print2");

        Thread print1 = new Thread(() -> {
            // 获取到锁（打印资格
            synchronized (lock) {
                while (true) {
                    if (printed == 2) {
                        log.debug("1");
                        printed = 1;
                        // 唤醒另一个线程
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "print1");

        print2.start();
        print1.start();
    }

}
