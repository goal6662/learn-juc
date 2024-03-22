package com.goal.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件变量
 */
@Slf4j(topic = "condition")
public class Demo04 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        // 创建条件变量 --- 休息室
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();


        lock.lock();
        // await前需要先获得锁
        condition1.await();

        // 唤醒前需要先获得锁
        // 唤醒该休息室的一个线程
        condition1.signal();
        // 唤醒该休息室的所有线程
        condition1.signalAll();


    }

}
