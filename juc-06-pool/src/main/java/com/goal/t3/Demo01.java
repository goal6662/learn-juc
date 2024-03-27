package com.goal.t3;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 */
@Slf4j(topic = "Timer")
public class Demo01 {

    public static void main(String[] args) {

        Timer timer = new Timer();

        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };


        timer.schedule(task1, 1000);

    }

}
