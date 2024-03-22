package com.goal.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示ReentrantLock的基本使用：可重入
 */
@Slf4j(topic = "c.Demo01")
public class Demo01 {

    static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        // 获取锁
        lock.lock();
        try {
            log.debug("enter main");
            m1();
        } finally {
            lock.unlock();
        }

    }

    public static void m1() {
        lock.lock();
        try {
            log.debug("enter m1");
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        lock.lock();
        try {
            log.debug("enter m2");
        } finally {
            lock.unlock();
        }
    }

}
