package com.goal.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread8Locks")
public class Thread8Locks {

    public static void main(String[] args) {

        Number n1 = new Number();
        Number n2 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }, "t1").start();


        new Thread(() -> {
            log.debug("begin");
            n2.b();
        }, "t2").start();
    }

}


@Slf4j(topic = "c.Number")
class Number {
    public synchronized static void a() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }

    public void c() {
        log.debug("3");
    }
}
