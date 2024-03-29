package com.goal.t1;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池的基本使用
 */
@Slf4j(topic = "pool")
public class Demo01 {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(2, 1000,
                TimeUnit.MILLISECONDS, 5, ((queue, task) -> {
                    // 死等
//                    queue.put(task);
                    // 超时等待
//                    queue.offer(task, 1, TimeUnit.SECONDS);
                    // 放弃执行
//                    log.debug("阻塞队列已满，放弃执行：{}", task);
                    // 调用者执行
//                    task.run();
                    // 抛出异常
                    throw new RuntimeException("任务执行失败, 停止执行后续任务" + task);
        }));

        for (int i = 0; i < 10; i++) {
            int j = i;
            // 当前任务执行不完，主线程会阻塞，这样不好
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("{}", j);
            });
        }
    }

}

@FunctionalInterface
interface RejectStrategy<T> {

    /**
     * 队列已满时的处理逻辑
     * @param queue 任务队列
     * @param task 任务
     */
    void reject(BlockingQueue<T> queue, T task);

}


@Slf4j(topic = "ThreadPool")
class ThreadPool {

    // 任务列表
    // 每一个任务都是一个可执行线程方法
    private final BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 线程数目
     */
    private final int coreSize;

    /**
     * 获取任务的时间
     */
    private final long timeout;

    /**
     * 获取任务的时间单位
     */
    private final TimeUnit timeUnit;

    /**
     * 队列已满时的处理
     */
    private final RejectStrategy<Runnable> rejectStrategy;

    public void execute(Runnable task) {
        // workers.size() 是读、workers.add(worker) 是写，需要保证其线程安全
        synchronized (workers) {
            if (workers.size() >= coreSize) {
                // 加入阻塞队列
//                taskQueue.put(task);

                // 交给调用者处理
                taskQueue.tryPut(rejectStrategy, task);

            } else {
                // 任务数目没有超过 coreSize 时，直接交给 worker 执行
                Worker worker = new Worker(task);
                log.debug("创建新的线程：{}", worker);
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
    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectStrategy<Runnable> rejectStrategy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        // 初始化任务列表
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectStrategy = rejectStrategy;
//        // 初始化线程
//        this.workers = new HashSet<>(coreSize);
    }

    // 线程工人
    private class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1. task 不为空，执行任务
            // 2. task 为空，从任务队列获取任务
//            while (task != null || (task = taskQueue.take()) != null) {
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.debug("开始执行任务, {}  {}", this, task);
                    task.run();
                } catch (Exception e) {
                    log.error("任务 {} 执行异常 {}", task, e.getMessage());
                } finally {
                    task = null;
                }
            }
            // 3. 没有任务需要执行
            synchronized (workers) {
                log.debug("没有可执行任务，线程被释放{}", this);
                workers.remove(this);
            }
        }
    }

}


/**
 * 阻塞队列
 * 线程池从该队列获取任务进行执行
 */
@Slf4j(topic = "BlockingQueue")
class BlockingQueue<T> {

    // 任务列表
    // ArrayDeque 性能较好
    private final Deque<T> deque = new ArrayDeque<>();

    // 锁
    private final ReentrantLock lock = new ReentrantLock();

    // 生产者条件变量
    private final Condition fullWaitSet = lock.newCondition();

    // 消费者条件变量
    private final Condition emptyWaitSet = lock.newCondition();

    // 容量
    private final int capacity;

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
                    log.debug("等待进入阻塞队列 {}", element);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 添加任务到任务列表
            deque.addLast(element);
            log.debug("进入阻塞队列 {}", element);
            // 唤醒消费者、添加一个唤醒一个
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }

    }


    public void offer(T element, long timeout, TimeUnit timeUnit) {
        // 加锁，否则可能有多个线程同时添加元素
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            // 判断容量是否达到上限
            while (this.size() >= capacity) {
                try {
                    if (nanos <= 0) {
                        log.warn("任务超时未响应执行失败：{}", element);
                        return;
                    }
                    log.debug("等待进入阻塞队列 {}", element);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 添加任务到任务列表
            deque.addLast(element);
            log.debug("进入阻塞队列 {}", element);
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

    public void tryPut(RejectStrategy<T> rejectStrategy, T element) {
        // 加锁，否则可能有多个线程同时添加元素
        lock.lock();
        try {
            // 判断容量是否达到上限
            if (this.size() < capacity) {
                // 添加任务到任务列表
                deque.addLast(element);
                log.debug("进入阻塞队列 {}", element);
                // 唤醒消费者、添加一个唤醒一个
                emptyWaitSet.signal();
            } else {
                rejectStrategy.reject(this, element);
            }
        } finally {
            lock.unlock();
        }
    }
}
