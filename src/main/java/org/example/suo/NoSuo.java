package org.example.suo;

public class NoSuo {
    // 共享变量a
    private static int a = 0;
    
    public static void main(String[] args) {
        System.out.println("多线程打印a++示例开始...");
        System.out.println("注意：没有使用锁和Atomic关键字，可能会出现线程安全问题");
        
        // 创建多个线程
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                a++; // 非原子操作
                System.out.println("线程1: a = " + a);
                try {
                    Thread.sleep(10); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-1");
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                a++; // 非原子操作
                System.out.println("线程2: a = " + a);
                try {
                    Thread.sleep(10); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-2");
        
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                a++; // 非原子操作
                System.out.println("线程3: a = " + a);
                try {
                    Thread.sleep(10); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-3");
        
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
        
        System.out.println("所有线程执行完毕，最终a的值: " + a);
        System.out.println("预期值应该是150，但实际可能不是150，这就是线程安全问题！");
    }
}