package com.goal.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Slf4j(topic = "c.Integer")
public class Demo01 {


    public static void main(String[] args) {

        update();

    }


    public static void myUpdate(AtomicInteger i, IntUnaryOperator operator) {

        while (true) {
            // 获取先前的值
            int prev = i.get();
            // 获取更新的值
            // 使用 一元整形 函数式接口，让用户自定义数据的运算
            int next = operator.applyAsInt(prev);
            if (i.compareAndSet(prev, next)) {
                break;
            }
        }

    }

    public static void update() {

        AtomicInteger i = new AtomicInteger(1);

        //                                读取值       设置值
        System.out.println(i.updateAndGet(value -> value * 10));    // 返回设置后的值 即：value * 10

    }

    public static void basic() {
        AtomicInteger i = new AtomicInteger(0);

        // 自增方法
        System.out.println(i.getAndIncrement());    // i++  0
        System.out.println(i.incrementAndGet());    // ++i  2

        // 自减
        System.out.println(i.decrementAndGet());    // --i  1
        System.out.println(i.getAndDecrement());    // i--  1

        // 获取、增加
        System.out.println(i.getAndAdd(5));     // 先获取再增加
        System.out.println(i.addAndGet(5));     // 先增加后获取
    }

}
