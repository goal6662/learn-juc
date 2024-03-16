package com.goal.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.multiThread")
public class Demo04 {

    public static void main(String[] args) {
        new Thread(() -> {
            int count = 0;
            while (true) {
                log.debug("running by t1 " + count++);
            }
        }, "t1").start();

        new Thread(() -> {
            int count = 0;
            while (true) {
                log.debug("running by t2 " + count++);
            }
        }, "t2").start();
    }

}
