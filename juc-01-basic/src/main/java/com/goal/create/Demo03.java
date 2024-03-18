package com.goal.create;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 获取线程的执行结果
 */
@Slf4j(topic = "c.Demo03")
public class Demo03 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // public class FutureTask<V> implements RunnableFuture<V>
        // public interface RunnableFuture<V> extends Runnable, Future<V>
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("running...");
                Thread.sleep(4000);
                return 100;
            }
        });

        Thread doTask = new Thread(task, "doTask");
        doTask.start();
        Thread.sleep(5000);
        // 获取线程执行结果
        log.debug("开始获取线程结果...");   // 根据时间安排：如果是异步的话，两次打印是连续的，几乎没有间隔时间
        log.debug("{}", task.get());    // 同步等待获取结果，如果已有结果立刻返回
    }

}
