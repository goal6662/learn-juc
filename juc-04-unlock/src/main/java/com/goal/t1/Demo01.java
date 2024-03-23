package com.goal.t1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "c.Demo01")
public class Demo01 {


    public static void main(String[] args) {
        Account account = new AccountUnsafe(10000);

        Account.demo(account);
        Account.demo(new AccountSafe(10000));
    }

}



class AccountSafe implements Account {

    private AtomicInteger balance;

    public AccountSafe(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        balance.addAndGet(-amount);
//        while (true) {
//            // 获取余额的最新值
//            int prev = balance.get();
//            // 获取要修改的值
//            int next = prev - amount;
//            // 比较并且设置，设置成功返回真
//            if (balance.compareAndSet(prev, next)) {
//                break;
//            }
//        }
    }
}

@AllArgsConstructor
class AccountUnsafe implements Account {

    private Integer balance;

    @Override
    public Integer getBalance() {
        return balance;
    }

    @Override
    public synchronized void withdraw(Integer amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }
}

interface Account {

    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    static void demo(Account account) {

        List<Thread> ts = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }

        long start = System.nanoTime();
        ts.forEach(Thread::start);

        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        long end = System.nanoTime();

        System.out.println(account.getBalance() + " cost: " + (end - start)/1000_000 + " ms");

    }

}
