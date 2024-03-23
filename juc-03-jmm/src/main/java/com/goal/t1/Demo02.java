package com.goal.t1;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Demo02")
public class Demo02 {

    public static void main(String[] args) {

    }

}

class Singleton {
    private Singleton() {}

    private volatile static Singleton INSTANCE = null;

    public static Singleton getInstance() {

        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}