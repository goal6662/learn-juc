package com.goal.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "multiCondition")
public class Exercise02 {

    private static final ReentrantLock lock = new ReentrantLock();

    // 开启两间休息室
    private static final Condition study = lock.newCondition();
    private static final Condition sleep = lock.newCondition();

    private static boolean hasCigarette = false;
    private static boolean hasTakeout = false;


    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            lock.lock();
            // 获取锁
            log.debug("获取到锁");
            try {
                // 有烟才干活
                while (!hasCigarette) {
                    log.debug("没烟，先歇会");
                    study.await();
                }

                log.debug("可以开始干活了");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }

        }, "小南").start();


        new Thread(() -> {
            lock.lock();
            // 获取锁
            log.debug("获取到锁");
            try {
                // 有烟才干活
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会");
                    sleep.await();
                }
                log.debug("可以开始睡觉了");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }

        }, "小女").start();

        Thread.sleep(1000);
        new Thread(() -> {
            lock.lock();
            try {
                hasTakeout = true;
                sleep.signal();
            } finally {
                lock.unlock();
            }

        }, "送外卖的").start();


        Thread.sleep(1000);
        new Thread(() -> {
            lock.lock();
            try {
                hasCigarette = true;
                study.signal();
            } finally {
                lock.unlock();
            }

        }, "送烟的").start();
    }
}