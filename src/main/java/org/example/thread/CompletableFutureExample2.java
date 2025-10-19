package org.example.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureExample2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("主线程开始...\n");

        // --- 1. 链式编排 (Chaining) - 任务先定义，后编排 ---
        // 场景：模拟一个用户下单流程：获取用户ID -> 获取用户信息 -> 打印最终的用户信息

        // 定义任务1: 获取用户ID
        Supplier<String> getUserIdTask = () -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务1: 正在从数据库获取用户ID...");
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务1: 用户ID获取完成.");
            return "user123";
        };

        // 定义任务2: 根据用户ID获取用户信息 (Function: input userId, output userInfo)
        Function<String, String> getUserInfoTask = userId -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务2: 根据用户ID " + userId + " 获取用户信息...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务2: 用户信息获取完成.");
            return "用户信息: " + userId + " - Name: Alice, Age: 30";
        };

        // 定义任务3: 打印最终的用户信息 (Consumer: input userInfo, no output)
        Consumer<String> printUserInfoTask = userInfo -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务3: 最终的用户信息为: " + userInfo);
        };

        // 链式编排任务：
        CompletableFuture<Void> chainedFuture = CompletableFuture.supplyAsync(getUserIdTask) // 启动第一个任务
                .thenApplyAsync(getUserInfoTask)   // 第一个任务完成后，执行第二个任务
                .thenAcceptAsync(printUserInfoTask); // 第二个任务完成后，执行第三个任务 (无返回值)

        System.out.println("主线程继续执行，不等待链式编排任务立即完成...");
        // 为了确保异步任务有时间执行，主线程需要等待一段时间。
        chainedFuture.get(); // 阻塞主线程，直到整个链式编排完成
        System.out.println("链式编排任务全部完成.\n");
        TimeUnit.MILLISECONDS.sleep(100); // 稍微等待一下，让日志打印顺序更自然


        System.out.println("--- 2. 组合任务 (Combination) - 任务先定义，后组合 ---");
        // 场景：模拟同时获取商品价格和库存，然后计算总价值

        // 定义任务A: 获取商品价格
        Supplier<Double> getPriceTask = () -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务A: 正在获取商品价格...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务A: 商品价格获取完成.");
            return 100.0;
        };

        // 定义任务B: 获取商品库存
        Supplier<Integer> getStockTask = () -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务B: 正在获取商品库存...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " -> 任务B: 商品库存获取完成.");
            return 50;
        };

        // 定义组合逻辑: 计算总价值 (BiFunction: input price, stock; output totalValue string)
        BiFunction<Double, Integer, String> calculateTotalValueTask = (price, stock) -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务C: 正在组合价格和库存...");
            double totalValue = price * stock;
            return "商品总价值: " + totalValue;
        };

        // 启动独立的任务A和任务B
        CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync(getPriceTask);
        CompletableFuture<Integer> stockFuture = CompletableFuture.supplyAsync(getStockTask);

        // 组合任务: 当价格和库存都准备好后，计算总价值
        CompletableFuture<String> combinedFuture = priceFuture.thenCombineAsync(stockFuture, calculateTotalValueTask);

        System.out.println("主线程继续执行，不等待组合任务立即完成...");

        // 获取组合任务的结果 (会阻塞主线程直到所有依赖任务完成并计算出结果)
        System.out.println("最终组合结果: " + combinedFuture.get());

        // --- 3. 等待所有任务完成 (CompletableFuture.allOf) 示例 ---
        // 场景：同时执行多个不相关的任务，等待所有任务都完成
        System.out.println("\n--- 3. 等待所有任务完成 (allOf) ---");
        CompletableFuture<Void> taskX = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务X: 执行中...");
            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println(Thread.currentThread().getName() + " -> 任务X: 完成.");
        });

        CompletableFuture<Void> taskY = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -> 任务Y: 执行中...");
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println(Thread.currentThread().getName() + " -> 任务Y: 完成.");
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(taskX, taskY);

        System.out.println("主线程继续执行，不等待所有任务立即完成...");
        allTasks.get(); // 阻塞主线程，直到 taskX 和 taskY 都完成
        System.out.println("所有任务 (X, Y) 都已完成.");


        System.out.println("\n主线程结束.");
    }
}