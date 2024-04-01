package com.goal.t3.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "wzry")
public class Exercise01 {

    public static void main(String[] args) throws InterruptedException {
        // 计数器
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        String[] all = new String[10];

        for (int i = 0; i < 10; i++) {
            // lambda 表达式使用
            // lambda 只能引用局部的常量
            int constI = i;
            pool.submit(() -> {
                for (int j = 0; j <= 100; j++) {
                    all[constI] = j + "%";
                    System.out.print("\r" + Arrays.toString(all));
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(3) * 100);
                }
                latch.countDown();
                return "";
            });
        }

        latch.await();
        System.out.println("\n游戏开始");
        pool.shutdown();
    }

}
