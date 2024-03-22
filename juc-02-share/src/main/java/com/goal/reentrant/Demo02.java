package com.goal.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可打断
 */
@Slf4j(topic = "interruptibly")
public class Demo02 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                // 如果没有竞争就会获取到锁
                // 如果有竞争就会进入阻塞队列，可以被其它线程 interrupt
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("没有获取到锁，返回");
                return;
            }

            try {
                log.debug("获取到锁");
            } finally {
                lock.unlock();
            }

        }, "t1");

        lock.lock();
        t1.start();

        TimeUnit.SECONDS.sleep(1);
        // 打断
        t1.interrupt();
    }

}
