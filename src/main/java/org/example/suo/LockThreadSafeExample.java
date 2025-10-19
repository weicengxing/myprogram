package org.example.suo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock接口实现线程安全
 */
public class LockThreadSafeExample {
    // 共享变量a
    private static int a = 0;
    // 创建锁对象
    private static final Lock lock = new ReentrantLock();
    
    public static void main(String[] args) {
        System.out.println("使用Lock接口实现线程安全示例开始...");
        
        // 记录开始时间（纳秒）
        long startTime = System.nanoTime();
        
        // 创建多个线程
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                lock.lock(); // 获取锁
                try {
                    a++; // 在锁保护下进行操作
                    System.out.println("线程1: a = " + a);
                } finally {
                    lock.unlock(); // 释放锁，放在finally块中确保一定会释放
                }
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Lock-Thread-1");
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                lock.lock(); // 获取锁
                try {
                    a++; // 在锁保护下进行操作
                    System.out.println("线程2: a = " + a);
                } finally {
                    lock.unlock(); // 释放锁，放在finally块中确保一定会释放
                }
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Lock-Thread-2");
        
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                lock.lock(); // 获取锁
                try {
                    a++; // 在锁保护下进行操作
                    System.out.println("线程3: a = " + a);
                } finally {
                    lock.unlock(); // 释放锁，放在finally块中确保一定会释放
                }
                try {
                    Thread.sleep(1); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Lock-Thread-3");
        
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
        
        System.out.println("所有线程执行完毕，最终a的值: " + a);
        System.out.println("使用Lock接口，最终值应该是900");
        System.out.println("总执行时间: " + totalTime + " 纳秒 (" + (totalTime / 1_000_000.0) + " 毫秒)");
    }
}
