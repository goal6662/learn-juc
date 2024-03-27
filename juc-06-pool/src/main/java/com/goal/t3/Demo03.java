package com.goal.t3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Goal
 */
@Slf4j
public class Demo03 {

    public static void main(String[] args) {

        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1);

        // 延期 1s 执行，之后 每隔 3s 执行一次
        // 线程任务开始执行时 3s 开始计时
        pool.scheduleAtFixedRate(() -> {
            log.debug("running");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 1, 3, TimeUnit.SECONDS);

    }

}
