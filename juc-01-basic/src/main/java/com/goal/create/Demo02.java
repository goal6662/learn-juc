package com.goal.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "t.Demo02")
public class Demo02 {

    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                log.debug("running");
            }
        };

        // lambda简化
        Runnable r2 = () -> log.debug("running by lambda");

        // 创建线程指定名称
        Thread t1 = new Thread(r1, "t1");
        Thread t2 = new Thread(r2, "t2");
        t1.start();
        t2.start();

    }



}
