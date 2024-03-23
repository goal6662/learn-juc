package com.goal.t1;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@Slf4j(topic = "Immutable")
public class Demo01 {

    public static void main(String[] args) {

        immutable();
    }

    public static void immutable() {
        DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log.debug("{}", stf.parse("1954-04-21"));
            }).start();
        }
    }

    public static void basic() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        log.debug("{}", sdf.parse("1954-04-21"));
                    } catch (ParseException e) {
                        log.error("{}", e.getMessage());
                    }
                }
            }).start();
        }
    }
}
