package org.example.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("主线程开始...");

        // --- 1. 链式编排 (Chaining) 示例 ---
        // 场景：模拟一个用户下单流程：获取用户ID -> 获取用户信息 -> 打印最终的用户信息

        // 任务1: 异步获取用户ID
        CompletableFuture<String> userIdFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务1: 正在从数据库获取用户ID...");
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务1: 用户ID获取完成.");
            return "user123"; // 返回用户ID
        });

        // 链式操作1: 当用户ID获取完成后，根据用户ID获取用户信息
        CompletableFuture<String> userInfoFuture = userIdFuture.thenApplyAsync(userId -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务2: 根据用户ID " + userId + " 获取用户信息...");
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务2: 用户信息获取完成.");
            return "用户信息: " + userId + " - Name: Alice, Age: 30"; // 返回用户信息
        });

        // 链式操作2: 当用户信息获取完成后，打印最终的用户信息
        // thenAcceptAsync 不返回结果，只对结果进行消费
        userInfoFuture.thenAcceptAsync(userInfo -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务3: 最终的用户信息为: " + userInfo);
        });

        System.out.println("主线程继续执行，不等待链式编排任务立即完成...");
        // 由于是异步执行，主线程会立即向下执行，不会阻塞。
        // 为了确保异步任务有时间执行，主线程需要等待一段时间。
        TimeUnit.SECONDS.sleep(3);

        System.out.println("\n--- 2. 组合任务 (Combination) 示例 ---");
        // 场景：模拟同时获取商品价格和库存，然后计算总价值

        // 任务A: 异步获取商品价格
        CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务A: 正在获取商品价格...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务A: 商品价格获取完成.");
            return 100.0;
        });

        // 任务B: 异步获取商品库存
        CompletableFuture<Integer> stockFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务B: 正在获取商品库存...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务B: 商品库存获取完成.");
            return 50;
        });

        // 组合任务: 当价格和库存都准备好后，计算总价值
        // thenCombine 会等待两个 CompletableFuture 都完成，然后将它们的结果作为参数传递给回调函数
        CompletableFuture<String> combinedFuture = priceFuture.thenCombine(stockFuture, (price, stock) -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务C: 正在组合价格和库存...");
            double totalValue = price * stock;
            return "商品总价值: " + totalValue;
        });

        System.out.println("主线程继续执行，不等待组合任务立即完成...");

        // 获取组合任务的结果 (会阻塞主线程直到所有依赖任务完成并计算出结果)
        // 这里的 get() 是阻塞的，它会等待 combinedFuture 完成。
        // combinedFuture 只有在 priceFuture 和 stockFuture 都完成后才会完成。
        System.out.println("最终组合结果: " + combinedFuture.get());

        System.out.println("主线程结束.");
    }
}