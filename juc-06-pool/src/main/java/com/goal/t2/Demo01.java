package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolExecutor
 */
@Slf4j(topic = "Demo01")
public class Demo01 {


    public static void main(String[] args) {



    }

    public static void single() {
        Executors.newSingleThreadExecutor();
    }

    public static void cached() {

        ExecutorService executorService = Executors.newCachedThreadPool();



    }


    public static void fixed() {
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            final AtomicInteger t = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "my_pool_t" + t.getAndIncrement());
            }
        });

        executorService.execute(() -> {
            log.debug("1");
        });

        executorService.execute(() -> {
            log.debug("2");
        });

        executorService.execute(() -> {
            log.debug("3");
        });
    }

}
