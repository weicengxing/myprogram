package org.example.suo;



import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS原子替换示例 - 使用真正的CAS操作
 */
public class CASExample {
    // 使用AtomicInteger，它内部使用真正的CAS操作
    private static AtomicInteger value = new AtomicInteger(0);


    public static void main(String[] args) {
        System.out.println("真正的CAS原子替换示例开始...");
        System.out.println("初始值: " + value.get());

        // 创建多个线程来演示CAS操作
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {


                boolean success = value.compareAndSet(0, 1);
                if(success){
                    System.out.println("线程1 Success!");
                    value.compareAndSet(1,0);
                    try {
                        Thread.sleep(50);//帮助线程切换，避免最先获得的线程一直获取锁，因为获取锁的逻辑很简单
                        // ，只有一行代码，只有在执行获取锁之前切换才能让另一个线程有机会，因此另一线程
                        //概率就小了
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    System.out.println("线程1 Failed!");
                }

            }
        }, "CAS-Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {


                boolean success = value.compareAndSet(0, 1);
                if(success){
                    System.out.println("线程2 Success!");
                    value.compareAndSet(1,0);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
                else{
                    System.out.println("线程2 Failed!");
                }

            }
        }, "CAS-Thread-2");

        // 启动线程
        thread1.start();
        thread2.start();

        // 等待线程完成
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最终值: " + value.get());

        // 演示其他CAS操作
        System.out.println("\n=== 其他CAS操作演示 ===");

    }

  }