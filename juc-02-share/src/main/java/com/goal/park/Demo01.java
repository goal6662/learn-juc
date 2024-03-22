package com.goal.park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.Demo01")
public class Demo01 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("park...");
            // 执行后线程处于 waiting 状态
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("unpark....");
        // 恢复线程运行
        LockSupport.unpark(t1);
    }

}
