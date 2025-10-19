package org.example.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadLocalExample {

    // 1. 定义一个 ThreadLocal 变量，存储整数类型
    private static final ThreadLocal<String> threadLocalValue = new ThreadLocal<>();

    public static void main(String[] args) {
        System.out.println("主线程开始...");

       ExecutorService executorService = Executors.newFixedThreadPool(3);
 // 创建一个固定大小的线程池
        
        // 用于存储 Future 对象的列表
        List<Future<String>> futures = new ArrayList<>();

        // 提交三个任务到线程池
        for (int i = 0; i < 4; i++) { // 循环改为4次，以验证等待机制
            final int taskId = i + 1;
            Future<String> future = executorService.submit(() -> {
                // 每个线程设置自己的 ThreadLocal 值
                String value = "Thread-" + taskId + " Data";
                threadLocalValue.set(value);
                System.out.println(Thread.currentThread().getName() + " 设置了 ThreadLocal 值为: " + threadLocalValue.get());

                try {
                    // 模拟线程工作
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "任务 " + taskId + " 被中断"; // 返回中断信息
                }

                // 每个线程获取自己的 ThreadLocal 值
                String result = Thread.currentThread().getName() + " 获取到 ThreadLocal 值为: " + threadLocalValue.get();
                System.out.println(result);

                // 清除 ThreadLocal 值，避免内存泄漏，特别是在线程池中
                threadLocalValue.remove();
                System.out.println(Thread.currentThread().getName() + " 清除了 ThreadLocal 值.");

                return "任务 " + taskId + " 执行完成，结果: " + value; // 返回任务结果
            });
            futures.add(future); // 将 Future 对象添加到列表中
        }

        // 关闭线程池
        executorService.shutdown();
        try {
            // 等待所有任务执行完毕
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("线程池等待终止时被中断.");
        }

        // 遍历 Future 列表，获取每个任务的执行结果
        System.out.println("\n--- 获取任务结果 ---");
        for (int i = 0; i < futures.size(); i++) {
            Future<String> future = futures.get(i);
            try {
                String result = future.get(); // 阻塞获取任务结果
                System.out.println("任务 " + (i + 1) + " 的结果: " + result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("获取任务 " + (i + 1) + " 结果时被中断.");
            } catch (ExecutionException e) {
                System.err.println("任务 " + (i + 1) + " 执行异常: " + e.getCause().getMessage());
            }
        }

        // 尝试在主线程中获取 ThreadLocal 值，应该为 null (或初始值，如果定义了 initialValue)
        System.out.println("\n主线程尝试获取 ThreadLocal 值: " + threadLocalValue.get());

        System.out.println("主线程结束.");
    }
}