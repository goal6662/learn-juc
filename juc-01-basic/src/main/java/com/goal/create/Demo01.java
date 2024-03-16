package com.goal.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Demo01 {

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                log.debug("running");
            }
        };

        // 设置线程的名称
        t1.setName("set");
        t1.start();

        // 使用lambda表达式返回了一个Runnable接口的实现类
        Thread t2 = new Thread(
                () -> log.debug("running by lambda"), "t2");
        t2.start();

        log.debug("running -- main");
    }

}
