package com.goal.monitor;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "c.Guarded")
public class Design01 {
    // 线程 1 等待 线程 2 的 下载结果
    public static void main(String[] args) {
        //  关联同一个锁
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            log.debug("等待资源下载完成...");
            List<String> object = (ArrayList<String>) guardedObject.get(1500);
            if (object != null)
                log.debug("资源大小为：{}", object.size());
        }, "use").start();

        new Thread(() -> {
            log.debug("开始下载资源...");
            try {
                // 执行下载操作，获取一个网页信息
                List<String> list = Downloader.download();
                guardedObject.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "prepare").start();
    }

}


class GuardedObject {

    // 结果
    private Object response;

    // 获取结果
    public Object get() {
        synchronized (this) {
            // 没有结果
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    // 设置最长等待时间
    public Object get(long timeout) {
        synchronized (this) {
            // 记录开始时间
            long begin = System.currentTimeMillis();

            while (response == null) {
                // 记录本轮经历的时间
                long passedTime = System.currentTimeMillis() - begin;
                long waitTime = timeout - passedTime;
                // 等待时间结束
                if (waitTime <= 0) {
                    break;
                }

                try {
                    // 使用waitTime代替timeout防止虚假唤醒的情况
                    this.wait(waitTime);
                    // 假设直接在这里添加 timeout，没有结果时也会一直死循环下去，没有超时等待的效果
//                    this.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }


    // 产生结果
    public void complete(Object response) {

        synchronized (this) {
            this.response = response;
            // 唤醒等待线程
            this.notifyAll();
        }

    }
}

class Downloader {

    public static List<String> download() throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL("https://baidu.com").openConnection();

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader
                = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

}