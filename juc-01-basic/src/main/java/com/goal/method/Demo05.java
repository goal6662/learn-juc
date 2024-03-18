package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * join 方法
 * 等待某个线程运行结束
 */
@Slf4j(topic = "c.Join")
public class Demo05 {

    static int r = 0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    public static void test1() throws InterruptedException {
        log.debug("开始");

        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("结束");
            r = 10;
        }, "test");

        t1.start();
        // 等待线程结束运行
//        t1.join();
        t1.join(1500);

        log.debug("结果为：{}", r);
        log.debug("结束");
    }

}
