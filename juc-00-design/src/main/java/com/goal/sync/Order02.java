package com.goal.sync;

import lombok.AllArgsConstructor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面向对象方式实现
 */
public class Order02 {

    public static void main(String[] args) {
        waitNotify();
//        awaitSignal();
//        parkUnpark();
    }

    public static void waitNotify() {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(() -> waitNotify.print("a", 1, 2)).start();
        new Thread(() -> waitNotify.print("b", 2, 3)).start();
        new Thread(() -> waitNotify.print("c", 3, 1)).start();
    }

    public static void awaitSignal() {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> awaitSignal.print("a", a, b)).start();
        new Thread(() -> awaitSignal.print("b", b, c)).start();
        new Thread(() -> awaitSignal.print("c", c, a)).start();

        // 唤醒 a 线程
        awaitSignal.lock();
        try {
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }

    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void parkUnpark() {

        ParkUnpark parkUnpark = new ParkUnpark(8);

        t1 = new Thread(() -> parkUnpark.print("a", t2), "t1");
        t2 = new Thread(() -> parkUnpark.print("b", t3), "t2");
        t3 = new Thread(() -> parkUnpark.print("c", t1), "t3");

        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }
}

@AllArgsConstructor
class WaitNotify {

    private int flag;

    private int loopNumber;

    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                // 不是自己的标记
                while (waitFlag != flag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}


@AllArgsConstructor
class AwaitSignal extends ReentrantLock {

    private int loopNumber;

    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            // 获取锁
            this.lock();
            try {
                current.await();
                System.out.print(str);
                next.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.unlock();
            }
        }
    }
}

@AllArgsConstructor
class ParkUnpark {
    private int loopNumber;
    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
}