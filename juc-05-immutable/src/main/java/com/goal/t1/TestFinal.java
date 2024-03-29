package com.goal.t1;

/**
 * 演示final原理
 */
public class TestFinal {

    static int A = 10;

    final static int B = Short.MAX_VALUE + 1;

    final int a = 20;
    final int b = Integer.MAX_VALUE;

    public void test1() {

    }
}

class UseFinal {

    public void test() {

        System.out.println(TestFinal.A);
        System.out.println(TestFinal.B);
        System.out.println(new TestFinal().a);
        System.out.println(new TestFinal().b);

        new TestFinal().test1();
    }

}

class UseFinal2 {
    public void test() {
        System.out.println(TestFinal.A);
    }
}
