package com.goal.t3.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示 CountDownLatch 的使用
 */
@Slf4j(topic = "cdl")
public class Demo01 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        pool.submit(() -> {
            log.debug("begin 1...");
            Thread.sleep(1000);
            latch.countDown();
            log.debug("end 1... {}", latch.getCount());
            return "";
        });

        pool.submit(() -> {
            log.debug("begin 2...");
            Thread.sleep(1500);
            latch.countDown();
            log.debug("end 2... {}", latch.getCount());
            return "";
        });

        pool.submit(() -> {
            log.debug("begin 3...");
            Thread.sleep(2000);
            latch.countDown();
            log.debug("end 3... {}", latch.getCount());
            return "";
        });

        pool.submit(() -> {
            log.debug("waiting...");
            latch.await();
            log.debug("ending... {}", latch.getCount());
            return "";
        });


    }

    private static void test01() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            try {
                log.debug("begin 1");
                Thread.sleep(1000);
                // 计数 -1
                latch.countDown();
                log.debug("end 1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                log.debug("begin 2");
                Thread.sleep(1500);
                latch.countDown();
                log.debug("end 2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();

        new Thread(() -> {
            try {
                log.debug("begin 3");
                Thread.sleep(2000);
                latch.countDown();
                log.debug("end 3");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t3").start();

        log.debug("waiting...");
        // 线程进入等待
        latch.await();
        log.debug("end...");
    }

}
