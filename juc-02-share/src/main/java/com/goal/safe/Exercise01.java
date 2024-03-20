package com.goal.safe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j(topic = "c.Exercise01")
public class Exercise01 {

    static Random random = new Random();

    public static int randomInt() {
        return random.nextInt(5) + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow ticketWindow = new TicketWindow(10000);

        // 所有线程的集合
        List<Thread> threadList = new ArrayList<>();

        // 买出的票数
        List<Integer> amountList = new Vector<>();

        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                // 临界区
                int amount = ticketWindow.sale(randomInt());
                amountList.add(amount);
            }, "sale" + (i + 1));

            // 仅被主线程使用，没有线程安全问题
            threadList.add(thread);
            thread.start();

        }

        for (Thread thread : threadList) {
            thread.join();
        }

        log.debug("余票：{}", ticketWindow.getCount());
        log.debug("卖出的票数：{}", amountList.stream().mapToInt(i -> i).sum());
    }

}

@Data
@AllArgsConstructor
class TicketWindow {

    private int count;

    // 售票
    // 临界区
    public synchronized int sale(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }

}
