package com.goal.method;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示 run 和 start 的区别
 */
@Slf4j(topic = "c.Demo01")
public class Demo01 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("running...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }, "t1");

        // 并没有开启线程，是由主线程执行的
//        t1.run();   // 14:43:54.707 [t1] DEBUG c.Demo01 - running...

        // 查看线程状态信息
        System.out.println(t1.getState());  // NEW
        t1.start();
        System.out.println(t1.getState());  // RUNNABLE
        log.debug("do other things");
    }

}
