package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 任务优先级
 */
@Slf4j(topic = "c.Demo04")
public class Demo04 {

    public static void main(String[] args) {

        Thread task1 = new Thread(() -> {
            int count = 0;
            while (true) {
                System.out.println("---->1 " + count++);
            }
        }, "task1");


        Thread task2 = new Thread(() -> {
            int count = 0;
            while (true) {
//                Thread.yield();
                System.out.println("                   ---->2 " + count++);
            }
        }, "task2");

        // 设置优先级，仅仅是对任务调度器的提示，实际效果取决于任务调度器
        task1.setPriority(Thread.MIN_PRIORITY);
        task2.setPriority(Thread.MAX_PRIORITY);

        task1.start();
        task2.start();

    }

}
