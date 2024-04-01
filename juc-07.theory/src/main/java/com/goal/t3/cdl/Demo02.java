package com.goal.t3.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Demo02 {

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            log.debug("task1, task2 finish...");
        });

        // 当变为 0 之后，下次再调用 await 值又会重新变为 2（初值
        // 使用 CyclicBarrier 的好处是可以避免重复创建 CountDownLatch
        for (int i = 0; i < 3; i++) {
            service.submit(() -> {
                log.debug("task1 begin...");
                try {
                    Thread.sleep(1000);
                    // 线程阻塞至此，等待为 0 后继续执行
                    cyclicBarrier.await();      // 2 - 1 = 1
                    log.debug("task1 end...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }, "task1");

            service.submit(() -> {
                log.debug("task2 begin...");
                try {
                    Thread.sleep(2000);
                    // 线程阻塞至此，等待为 0 后继续执行
                    cyclicBarrier.await();      // 1 - 1 = 0
                    log.debug("task2 end...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }, "task2");
        }

        service.shutdown();
    }

}
