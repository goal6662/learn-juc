package com.goal.t1;

import javafx.concurrent.Worker;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池的基本使用
 */
@Slf4j(topic = "pool")
public class Demo01 {

    public static void main(String[] args) {

    }

}


class ThreadPool {

    // 任务列表
    // 每一个任务都是一个可执行线程方法
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 线程数目
     */
    private int coreSize;

    /**
     * 获取任务的时间
     */
    private long timeout;

    /**
     * 获取任务的时间单位
     */
    private TimeUnit timeUnit;

    public void execute(Runnable task) {
        // workers.size() 是读、workers.add(worker) 是写，需要保证其线程安全
        synchronized (workers) {
            if (workers.size() >= coreSize) {
                // 加入阻塞队列
                taskQueue.put(task);
            } else {
                // 任务数目没有超过 coreSize 时，直接交给 worker 执行
                Worker worker = new Worker(task);
                // 添加至工人列表
                workers.add(worker);
                worker.start();
            }
        }
    }


    /**
     * 线程池构造方法
     * @param coreSize 线程数目
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param queueCapacity 任务列表容量
     */
    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        // 初始化任务列表
        this.taskQueue = new BlockingQueue<>(queueCapacity);
//        // 初始化线程
//        this.workers = new HashSet<>(coreSize);
    }

    // 线程工人
    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1. task 不为空，执行任务
            // 2. task 为空，从任务队列获取任务
            while (task != null || (task = taskQueue.take()) != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    System.out.println("任务执行异常" + task);
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            // 3. 没有任务需要执行
            synchronized (workers) {
                workers.remove(this);
            }
        }
    }

}


/**
 * 阻塞队列
 * 线程池从该队列获取任务进行执行
 */
class BlockingQueue<T> {

    // 任务列表
    // ArrayDeque 性能较好
    private Deque<T> deque = new ArrayDeque<>();

    // 锁
    private ReentrantLock lock = new ReentrantLock();

    // 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        // 任务列表是一个共享变量
        lock.lock();
        try {
            // 将时间转为纳秒
            long nanos = unit.toNanos(timeout);
            // 当前没有任务需要执行，进入阻塞队列
            while (deque.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    // 剩余的等待时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取并删除第一个任务
            T element = deque.removeFirst();
            // 唤醒生产者
            fullWaitSet.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }


    // 阻塞获取任务进行执行
    public T take() {
        // 任务列表是一个共享变量
        lock.lock();

        try {
            // 当前没有任务需要执行，进入阻塞队列
            while (deque.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取并删除第一个任务
            T element = deque.removeFirst();
            // 唤醒生产者
            fullWaitSet.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T element) {
        // 加锁，否则可能有多个线程同时添加元素
        lock.lock();

        try {
            // 判断容量是否达到上限
            while (this.size() >= capacity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 添加任务到任务列表
            deque.addLast(element);
            // 唤醒消费者、添加一个唤醒一个
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }

    }

    // 获取队列大小
    public int size() {
        lock.lock();

        try {
            return deque.size();
        } finally {
            lock.unlock();
        }
    }

}
