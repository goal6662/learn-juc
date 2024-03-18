package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 演示Java线程状态
 */
@Slf4j(topic = "c.Demo07")
public class Demo07 {

    public static void main(String[] args) {

        // 1. NEW 初始化
        Thread t1 = new Thread(() -> {
            System.out.println("t1.running...");
        });

        // 2. RUNNABLE 运行中
        Thread t2 = new Thread(() -> {
            while (true) {
            }
        }, "t2");
        t2.start();

        // 3. WAITING 等待中
        Thread t3 = new Thread(() -> {
            try {
                // 等待 t2 运行结束
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t3");
        t3.start();

        // 4. TIMED_WAITING 超时等待
        Thread t4 = new Thread(() -> {
            synchronized (Demo07.class) {
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t4");
        t4.start();

        // 5. BLOCKED 阻塞中
        Thread t5 = new Thread(() -> {
            synchronized (Demo07.class) {
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t5");
        t5.start();

        // 6. TERMINATED 终止
        Thread t6 = new Thread(() -> {
            System.out.println("t6.running...");
        }, "t6");
        t6.start();

        log.debug("t1 state: {}", t1.getState());
        log.debug("t2 state: {}", t2.getState());
        log.debug("t3 state: {}", t3.getState());
        log.debug("t4 state: {}", t4.getState());
        log.debug("t5 state: {}", t5.getState());
        log.debug("t6 state: {}", t6.getState());

    }

}
