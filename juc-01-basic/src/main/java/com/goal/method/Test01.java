package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Test01")
public class Test01 {

    public static void main(String[] args) throws InterruptedException {
        // 洗水壶
        Thread t1 = new Thread(() -> {
            log.info("正在洗水壶中");
            try {
                TimeUnit.SECONDS.sleep(1);
                log.info("洗水壶完成了");
            } catch (InterruptedException e) {
                log.error("发生什么事了");
            }
        }, "wash");


        // 烧开水
        Thread t2 = new Thread(() -> {
            try {
                // 洗完水壶才能烧
                t1.join();
                log.info("正在烧开水");
                TimeUnit.SECONDS.sleep(15);
                log.info("烧开水完成了");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        // 拿东西
        Thread t3 = new Thread(() -> {
            log.info("正在洗茶壶，洗茶杯，拿茶叶");
            try {
                TimeUnit.SECONDS.sleep(4);
                log.info("洗茶壶，洗茶杯，拿茶叶完成了");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();
        t2.start();
        t1.start();

        // 烧完水、准备好东西才能泡茶
        t3.join();
        t2.join();

        log.info("茶泡好了");
    }

}
