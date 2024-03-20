package com.goal.sync;

/**
 * synchronized语法
 */
public class Demo01 {

    static int count = 0;

    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                synchronized (lock) {
                    count++;
                    System.out.println("increment...");
                }
            }

        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                synchronized (lock) {
                    count--;
                    System.out.println("decrement...");
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("count = " + count);
    }

}