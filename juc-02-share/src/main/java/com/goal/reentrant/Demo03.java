package com.goal.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示锁超时
 */
@Slf4j(topic = "c.Timeout")
public class Demo03 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            // 尝试获取锁，获取不到立刻返回
//            if (!lock.tryLock()) {
//                log.debug("获取不到锁");
//                return;
//            }

            // 尝试在1s之内获取到锁
            // 等待期间被打断，抛出InterruptException
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                log.debug("获取不到锁...");
                return;
            }

            try {
                log.debug("获得到了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得到锁");
        t1.start();
        // 打断线程
//        t1.interrupt();

    }

}
