package com.goal.create;

public class Demo05 {

    public static void main(String[] args) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                method1(20);
            }
        };

        thread.setName("t1");
        thread.start();

        method1(10);
    }


    public static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    public static Object method2() {
        Object n = new Object();
        return n;
    }

}
