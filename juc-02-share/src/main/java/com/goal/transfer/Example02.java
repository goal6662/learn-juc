package com.goal.transfer;

import lombok.extern.slf4j.Slf4j;

/**
 * WAITING <--> RUNNABLE
 */
@Slf4j(topic = "runWait")
public class Example02 {

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    log.debug("线程被唤醒了...");
                } catch (InterruptedException e) {
                    log.warn("线程被打断了...");
                }
                log.debug("其它代码...");
            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    log.debug("线程被唤醒了...");
                } catch (InterruptedException e) {
                    log.warn("线程被打断了...");
                }
                log.debug("其它代码...");
            }
        }, "t2");
        t1.start();
        t2.start();

        Thread.sleep(500);
        synchronized (lock) {
//        lock.notify();
            // 获得锁之后才能执行 notify
            lock.notifyAll();
        }

        log.debug("t1.state: {}", t1.getState());
        log.debug("t2.state: {}", t2.getState());

    }

}
