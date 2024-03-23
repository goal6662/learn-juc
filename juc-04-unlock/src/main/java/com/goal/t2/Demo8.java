package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 高性能的累加器
 * 性能差距 2~5 倍
 */
@Slf4j(topic = "adder")
public class Demo8 {

    public static void main(String[] args) {
        // atomicLong 实现
        demo(
                () -> new AtomicLong(0),
                (adder) -> adder.getAndIncrement()
        );


        // longAdder 实现
        demo(
                () -> new LongAdder(),
                adder -> adder.increment()
        );

    }

    /**
     *
     * @param addSupplier 累加器
     * @param action    累计操作
     * @param <T>
     */
    private static <T> void demo(
            Supplier<T> addSupplier,
            Consumer<T> action
    ) {

        T adder = addSupplier.get();
        // 4个线程，每人累加 50w
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }

        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        long end = System.nanoTime();

        System.out.println(adder + " cost: " + (end - start)/1000_000);

    }

}
