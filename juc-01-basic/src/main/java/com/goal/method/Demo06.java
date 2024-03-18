package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Interrupt")
public class Demo06 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.debug("sleep...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "interrupt");

        thread.start();
        Thread.sleep(1000);

        log.debug("interrupt");
        thread.interrupt();
        // 睡眠时被打断
        log.debug("打断标记：{}", thread.isInterrupted());
    }

}
