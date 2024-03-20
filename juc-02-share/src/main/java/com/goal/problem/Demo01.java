package com.goal.problem;

public class Demo01 {

    static int count = 0;

    static void increment() {
        count++;
    }

    static void decrement() {
        count--;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread increment = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                increment();
            }
        }, "increment");
        increment.start();

        Thread decrement = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                decrement();
            }
        }, "decrement");
        decrement.start();

        Thread.sleep(50);
        System.out.println("count = " + count);
    }

}
