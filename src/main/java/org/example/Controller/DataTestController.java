package org.example.Controller;

import org.example.service.DataGeneratorService;
import org.example.service.MySQLDataService;
import org.example.service.RedisDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataTestController {
    
    @Autowired
    private DataGeneratorService dataGeneratorService;
    
    @Autowired
    private MySQLDataService mySQLDataService;
    
    @Autowired
    private RedisDataService redisDataService;
    
    /**
     * 生成测试数据
     */
    @GetMapping("/generate")
    public String generateData() {
        dataGeneratorService.generateTestData();
        return "数据生成完成！";
    }
    
    /**
     * 插入数据到MySQL
     */
    @GetMapping("/mysql/insert")
    public String insertToMySQL() {
        mySQLDataService.insertDataFromFile();
        return "MySQL数据插入完成！";
    }
    
    /**
     * 存储数据到Redis
     */
    @GetMapping("/redis/store")
    public String storeToRedis() {
        redisDataService.storeDataFromFile();
        return "Redis数据存储完成！";
    }
    
    /**
     * 测试MySQL性能
     */
    @GetMapping("/mysql/performance")
    public String testMySQLPerformance() {
        mySQLDataService.testMySQLPerformance();
        return "MySQL性能测试完成！";
    }
    
    /**
     * 测试Redis性能
     */
    @GetMapping("/redis/performance")
    public String testRedisPerformance() {
        redisDataService.testRedisPerformance();
        return "Redis性能测试完成！";
    }
    
    /**
     * 显示Redis内存使用情况
     */
    @GetMapping("/redis/memory")
    public String showRedisMemory() {
        redisDataService.showMemoryUsage();
        return "Redis内存使用情况已显示！";
    }
    
    /**
     * 一键执行所有操作
     */
    @GetMapping("/run-all")
    public String runAll() {
        System.out.println("🚀 开始执行所有操作...");
        
        // 1. 生成数据
        System.out.println("\n📋 步骤1: 生成测试数据");
        dataGeneratorService.generateTestData();
        
        // 2. 插入MySQL
        System.out.println("\n📋 步骤2: 插入数据到MySQL");
        mySQLDataService.insertDataFromFile();
        
        // 3. 存储Redis
        System.out.println("\n📋 步骤3: 存储数据到Redis");
        redisDataService.storeDataFromFile();
        
        // 4. 性能对比测试
        System.out.println("\n📋 步骤4: MySQL vs Redis 性能对比");
        System.out.println("==================================================");
        mySQLDataService.testMySQLPerformance();
        redisDataService.testRedisPerformance();
        
        // 5. 显示Redis内存使用
        System.out.println("\n📋 步骤5: 显示Redis内存使用");
        redisDataService.showMemoryUsage();
        
        System.out.println("\n✅ 所有操作完成！");
        return "所有操作执行完成！";
    }
}
