package com.goal.multi;

import lombok.extern.slf4j.Slf4j;

/**
 * 排查死锁
 */
@Slf4j(topic = "c.DeadLock")
public class DeadLock {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick();
        Chopstick c2 = new Chopstick();
        Chopstick c3 = new Chopstick();
        Chopstick c4 = new Chopstick();
        Chopstick c5 = new Chopstick();

        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();

    }

}

@Slf4j(topic = "哲学家开吃：")
class Philosopher extends Thread {

    private final Chopstick left;
    private final Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (left) {
                synchronized (right) {
//                    log.debug(super.getName() + "开始就餐");
                    log.debug("eating...");
                }
            }
        }
    }
}

class Chopstick {
}
