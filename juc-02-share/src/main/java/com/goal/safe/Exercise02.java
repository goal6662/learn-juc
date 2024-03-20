package com.goal.safe;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j(topic = "c.Exercise02")
public class Exercise02 {

    static Random random = new Random();

    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }

    public static void main(String[] args) throws InterruptedException {

        Account accountA = new Account(1000);
        Account accountB = new Account(1000);

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountA.transferTo(accountB, randomAmount());
            }
        }, "a");

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountB.transferTo(accountA, randomAmount());
            }
        }, "b");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        log.debug("total: A - {}  B - {}", accountA.getMoney(), accountB.getMoney());

    }

}


@Getter
class Account {

    private int money;

    public Account(int money) {
        this.money = money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void transferTo(Account target, int amount) {
        // 共享变量：this.money target.money
        synchronized (Account.class) {
            if (this.money >= amount) {
                this.money -= amount;
                target.setMoney(target.getMoney() + amount);
            }
        }

    }
}