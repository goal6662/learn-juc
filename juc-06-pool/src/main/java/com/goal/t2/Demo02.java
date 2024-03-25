package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 提交方法
 */
@Slf4j(topic = "Submit")
public class Demo02 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        invokeAll();

    }

    public static void invokeAll() throws InterruptedException {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

        List<Future<String>> futures = fixedThreadPool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin 1");
                    Thread.sleep(1000);
                    log.debug("end 1");
                    return "1";
                },
                () -> {
                    log.debug("begin 2");
                    Thread.sleep(500);
                    log.debug("end 2");
                    return "2";
                },
                () -> {
                    log.debug("begin 3");
                    Thread.sleep(2000);
                    log.debug("end 3");
                    return "3";
                }
        ));

        futures.forEach((future) -> {
            try {
                log.debug(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void single() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<String> future = pool.submit(() -> {
            log.debug("running...");
            Thread.sleep(1000);
            return "ok";
        });

        log.debug(future.get());
    }

}
