package com.goal.monitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Monitor")
public class Demo01 {

    static final Object lock = new Object();

    static int count = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            count++;
        }
    }

}
