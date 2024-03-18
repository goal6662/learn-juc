package com.goal.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 演示两阶段终止模式
 */
public class Example {

    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination termination = new TwoPhaseTermination();

        termination.start();

//        Thread.sleep(3500); // 睡眠时被打断
        Thread.sleep(2000);

        termination.stop();

    }

}

@Slf4j
class TwoPhaseTermination {
    private Thread monitor;

    // 启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {

                if(Thread.currentThread().isInterrupted()) {
                    log.debug("料理后事...");
                    break;
                }

                try {
                    log.debug("查询监控状况...");
                    TimeUnit.SECONDS.sleep(2);  // 睡眠过程中(sleep, 以及 join、wait)被打断，会清除打断标记(设置为false)
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    // 重新设置打断标记
                    Thread.currentThread().interrupt();
                }

            }
        }, "monitor");
        monitor.start();
    }

    // 终止监控线程
    public void stop() {
        monitor.interrupt();
    }
}
