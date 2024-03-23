package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ABA问题
 */
@Slf4j(topic = "ABA")
public class Demo03 {

    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {

        log.debug("main start...");

        // 获取之前的值
        String prev = ref.get();

        // 该方法内实现了 A -> B -> A 的转换，虽说对实际业务没有什么影响，但存在安全隐患
        other();
        TimeUnit.SECONDS.sleep(1);

        // 尝试修改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C"));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.get(), "B"));
        }, "t1").start();

        TimeUnit.MILLISECONDS.sleep(500);

        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.get(), "A"));
        }, "t2").start();
    }

}
