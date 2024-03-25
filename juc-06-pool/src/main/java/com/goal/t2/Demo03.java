package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * 关闭线程池
 */
@Slf4j(topic = "Shutdown")
public class Demo03 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> r1 = pool.submit(() -> {
            log.debug("task 1 running...");
            Thread.sleep(1000);
            log.debug("task 1 finish...");
            return 1;
        });

        Future<Integer> r2 = pool.submit(() -> {
            log.debug("task 2 running...");
            Thread.sleep(1000);
            log.debug("task 2 finish...");
            return 2;
        });

        Future<Integer> r3 = pool.submit(() -> {
            log.debug("task 3 running...");
            Thread.sleep(1000);
            log.debug("task 3 finish...");
            return 3;
        });

        log.debug("shutdown");
//        pool.shutdown();
//        // 阻塞至此，直至 线程 运行结束
//        pool.awaitTermination(3, TimeUnit.MINUTES);
//        log.debug("others");

        List<Runnable> runnableList = pool.shutdownNow();
        log.debug("runnable: {}", runnableList.size());


        // 结束之后无法提交新任务
        // Exception in thread "main" java.util.concurrent.RejectedExecutionException
//        Future<Integer> r4 = pool.submit(() -> {
//            log.debug("task 4 running...");
//            Thread.sleep(1000);
//            log.debug("task 4 finish...");
//            return 4;
//        });
    }

}
