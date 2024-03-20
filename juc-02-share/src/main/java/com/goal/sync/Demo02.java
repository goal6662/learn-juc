package com.goal.sync;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Demo02")
public class Demo02 {

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        log.debug("start...");
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                room.increment();
            }
        }, "increment");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                room.decrement();
            }
        }, "decrement");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("count: {}", room.getCount());
    }
}


class Room {
    private int count = 0;

    public void increment() {
        synchronized (this) {
            count++;
        }
    }

    public void decrement() {
        synchronized (this) {
            count--;
        }
    }

    public int getCount() {
        // 为防止获取时还为写入，获取时也要加锁
        synchronized (this) {
            return count;
        }
    }
}
