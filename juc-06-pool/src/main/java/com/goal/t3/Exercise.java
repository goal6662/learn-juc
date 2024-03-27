package com.goal.t3;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 * 每周一 15：00 执行任务
 */
@Slf4j(topic = "timeTask")
public class Exercise {

    public static void main(String[] args) {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取本周周一 15:00
        LocalDateTime time = now.withHour(14).withMinute(53).withSecond(30).withNano(0).with(DayOfWeek.MONDAY);

        // 当前日期在周一之后 返回下周一时间
        if (now.isAfter(time)) {
            time = time.plusWeeks(1);
        }

        System.out.println(time);

        // 任务延期执行的时间
        long initialDelay = Duration.between(now, time).toMillis();

        // 任务调度间隔时间
        long period = 1000 * 5;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            log.debug("running...");
        }, initialDelay, period, TimeUnit.MILLISECONDS);

    }

}
