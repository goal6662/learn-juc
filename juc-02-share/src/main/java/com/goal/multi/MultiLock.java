package com.goal.multi;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "multiLock")
public class MultiLock {

    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();

        new Thread(() -> {
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "小南").start();

        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "小女").start();
    }

}


@Slf4j(topic = "bigRoom")
class BigRoom {

    private final Object study = new Object();
    private final Object sleep = new Object();

    public void study() throws InterruptedException {
        synchronized (study) {
            log.debug("学习 1 秒钟");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void sleep() throws InterruptedException {
        synchronized (sleep) {
            log.debug("睡觉 2 秒钟");
            TimeUnit.SECONDS.sleep(2);
        }
    }

}
