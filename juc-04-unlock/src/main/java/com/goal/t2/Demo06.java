package com.goal.t2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 原子数组
 */
public class Demo06 {


    public static void main(String[] args) {
        demo(
                () -> new int[10],
                (array) -> array.length,
                (array, index) -> array[index]++,
                (array) -> System.out.println(Arrays.toString(array))
        );


        demo(
                () -> new AtomicIntegerArray(10),
                AtomicIntegerArray::length,
                (array, index) -> array.incrementAndGet(index),
                (array) -> System.out.println(array.toString())
        );
    }


    /**
     * 测试函数
     * @param arraySupplier 供给者：() -> 结果，返回一个数组 T
     * @param arrayLength   函数（一对一映射）：(参数) -> 结果，返回数组的长度
     * @param putConsumer   bi-两个消费者：(参数1, 参数2) -> void，
     * @param printConsumer 消费者：(参数) -> void，打印数组 T
     * @param <T> 泛型
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> arrayLength,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer
    ) {

        List<Thread> ts = new ArrayList<>();
        // 调用函数式接口获取数据
        T array = arraySupplier.get();
        // 获取数组长度
        int length = arrayLength.apply(array);

        for (int i = 0; i < length; i++) {
            // 每个线程对数组进行 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % length);
                }
            }));
        }

        // 启动所有线程
        ts.forEach(Thread::start);
        // 等待所有线程结束
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 打印结果
        printConsumer.accept(array);
    }

}
