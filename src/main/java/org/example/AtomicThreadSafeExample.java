package org.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用AtomicInteger实现线程安全
 */
public class AtomicThreadSafeExample {
    // 使用AtomicInteger作为共享变量
    private static AtomicInteger a = new AtomicInteger(0);
    
    public static void main(String[] args) {
        System.out.println("使用AtomicInteger实现线程安全示例开始...");
        
        // 记录开始时间（纳秒）
        long startTime = System.nanoTime();
        
        // 创建多个线程
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                int currentValue = a.incrementAndGet(); // 原子性递增操作
                System.out.println("线程1: a = " + currentValue);
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Atomic-Thread-1");
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                int currentValue = a.incrementAndGet(); // 原子性递增操作
                System.out.println("线程2: a = " + currentValue);
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Atomic-Thread-2");
        
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                int currentValue = a.incrementAndGet(); // 原子性递增操作
                System.out.println("线程3: a = " + currentValue);
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Atomic-Thread-3");
        
        // 启动所有线程
        thread1.start();
        thread2.start();
        thread3.start();
        
        // 等待所有线程完成
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 记录结束时间（纳秒）
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        
        System.out.println("所有线程执行完毕，最终a的值: " + a.get());
        System.out.println("使用AtomicInteger，最终值应该是900");
        System.out.println("总执行时间: " + totalTime + " 纳秒 (" + (totalTime / 1_000_000.0) + " 毫秒)");
    }
}
