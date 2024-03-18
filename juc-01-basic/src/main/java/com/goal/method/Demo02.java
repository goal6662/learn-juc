package com.goal.method;

import lombok.extern.slf4j.Slf4j;

/**
 * sleep
 */
@Slf4j
public class Demo02 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state: {}", t1.getState());   // RUNNABLE

        Thread.sleep(500);
        log.debug("t1 state: {}", t1.getState());   // TIMED_WAITING

    }

}
