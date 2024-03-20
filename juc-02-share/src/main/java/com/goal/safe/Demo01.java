package com.goal.safe;

import java.util.ArrayList;
import java.util.List;

public class Demo01 {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
//        ThreadUnsafe threadUnsafe = new ThreadUnsafe();
//
//        for (int i = 0; i < THREAD_NUMBER; i++) {
//            new Thread(() -> threadUnsafe.method(LOOP_NUMBER), "thread" + (i + 1)).start();
//        }

        ThreadSafe threadSafe = new ThreadSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> threadSafe.method(LOOP_NUMBER), "threadSafe" + (i + 1)).start();
        }

    }

}


class ThreadUnsafe {

    List<String> list = new ArrayList<>();

    public void method(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }

}

class ThreadSafe {

    public void method(int loopNumber) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }

    }

    private void method2(List<String> list) {
        list.add("1");
    }

    private void method3(List<String> list) {
        list.remove(0);
    }

}