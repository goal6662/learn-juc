package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j(topic = "stamped")
public class Demo04 {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {

        log.debug("main start...");

        // 获取之前的值
        int stamp = ref.getStamp();
        log.debug("stamp: {}", stamp);
        String prev = ref.getReference();

        other();
        TimeUnit.SECONDS.sleep(1);

        // 尝试修改为 C
        log.debug("stamp: {}", ref.getStamp());
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.debug("stamp: {}", stamp);
            log.debug("change A->B {}",
                    ref.compareAndSet(ref.getReference(), "B", stamp, stamp + 1));
        }, "t1").start();

        TimeUnit.MILLISECONDS.sleep(500);

        new Thread(() -> {
            int stamp = ref.getStamp();
            log.debug("stamp: {}", stamp);
            log.debug("change B->A {}",
                    ref.compareAndSet(ref.getReference(), "A", stamp, stamp + 1));
        }, "t2").start();
    }

}
