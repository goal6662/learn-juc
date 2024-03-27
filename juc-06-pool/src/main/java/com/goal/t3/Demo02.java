package com.goal.t3;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Goal
 */
@Slf4j(topic = "timer")
public class Demo02 {

    public static void main(String[] args) {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        pool.schedule(() -> {
            log.debug("task 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);


        pool.schedule(() -> {
            log.debug("task 2");

        }, 1, TimeUnit.SECONDS);

    }

}
