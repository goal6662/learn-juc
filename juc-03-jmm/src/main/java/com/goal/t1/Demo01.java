package com.goal.t1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

public class Demo01 {


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

    private volatile static boolean stop = false;

    // 判断是否已经开启
    private boolean starting = false;

    // 启动监控线程
    public void start() {
        // 只有第一次进来是假，其它都是真
        synchronized (this) {
            if (starting) {
                return;
            }
            starting = true;
        }
        monitor = new Thread(() -> {
            while (true) {

                if(stop) {
                    log.debug("料理后事...");
                    break;
                }

                try {
                    log.debug("查询监控状况...");
                    TimeUnit.SECONDS.sleep(2);  // 睡眠过程中(sleep, 以及 join、wait)被打断，会清除打断标记(设置为false)
                } catch (InterruptedException e) {
                    // 重新设置打断标记
                    Thread.currentThread().interrupt();
                }

            }
        }, "monitor");
        monitor.start();
    }

    // 终止监控线程
    public void stop() {
        stop = true;
        monitor.interrupt();
        starting = false;
    }

}
