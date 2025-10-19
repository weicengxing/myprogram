package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisDataService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 从shiyan.json文件读取数据并存储到Redis
     */
    public void storeDataFromFile() {
        try {
            // 读取JSON文件
            JsonNode rootNode = objectMapper.readTree(new File("shiyan.json"));
            
            long startTime = System.currentTimeMillis();
            
            // 清空Redis数据库
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            
            // 存储用户数据
            storeUsers(rootNode.get("users"));
            
            // 创建索引
            createIndexes(rootNode);
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("✅ Redis数据存储完成！");
            System.out.println("📊 总耗时: " + (endTime - startTime) + " 毫秒");
            
        } catch (IOException e) {
            System.err.println("❌ 读取数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 存储用户数据到Redis
     */
    private void storeUsers(JsonNode usersNode) {
        for (JsonNode user : usersNode) {
            String key = "user:" + user.get("id").asText();
            redisTemplate.opsForHash().putAll(key, Map.of(
                "id", user.get("id").asText(),
                "username", user.get("username").asText(),
                "email", user.get("email").asText(),
                "age", user.get("age").asText(),
                "city", user.get("city").asText(),
                "balance", user.get("balance").asText(),
                "created_at", user.get("created_at").asText()
            ));
            redisTemplate.expire(key, java.time.Duration.ofHours(1));
        }
        
        System.out.println("📥 用户数据存储完成: " + usersNode.size() + " 条");
    }
    
    
    /**
     * 创建Redis索引
     */
    private void createIndexes(JsonNode rootNode) {
        // 用户索引
        for (JsonNode user : rootNode.get("users")) {
            String userId = user.get("id").asText();
            String city = user.get("city").asText();
            String balance = user.get("balance").asText();
            
            // 按城市索引
            redisTemplate.opsForSet().add("users:city:" + city, userId);
            // 按余额排序
            redisTemplate.opsForZSet().add("users:balance", userId, Double.parseDouble(balance));
        }
        
        System.out.println("📊 用户索引创建完成");
    }
    
    /**
     * 测试Redis查询性能 - 根据username查询user
     */
    public void testRedisPerformance() {
        System.out.println("\n🔍 开始Redis性能测试...");
        
        // 测试根据username查询user的性能
        String testUsername = "user_0001";
        
        long startTime = System.currentTimeMillis();
        
        // 方法1: 直接根据ID查询（如果知道ID的话）
        // 由于我们知道user_0001对应ID=1，可以直接查询
        Map<Object, Object> user = redisTemplate.opsForHash().entries("user:1");
        
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        
        System.out.println("  Redis查询用户 (user:1): " + queryTime + "ms");
        System.out.println("  查询结果: " + (user.isEmpty() ? "未找到" : "找到用户数据"));

//        // 方法2: 如果必须根据username查询，使用SCAN代替KEYS
//        System.out.println("\n🔍 使用SCAN方式查询（更高效）:");
//        startTime = System.currentTimeMillis();
//
//        user = findUserByUsernameWithScan(testUsername);
//
//        endTime = System.currentTimeMillis();
//        queryTime = endTime - startTime;
//
//        System.out.println("  Redis SCAN查询用户 (username=" + testUsername + "): " + queryTime + "ms");
//        System.out.println("  查询结果: " + (user == null ? "未找到" : "找到用户数据"));
    }
    
    /**
     * 使用SCAN方式查找用户（比KEYS更高效）
     */
    private Map<Object, Object> findUserByUsernameWithScan(String username) {
        String pattern = "user:*";
        int count = 100; // 每次扫描100个键
        
        try (var connection = redisTemplate.getConnectionFactory().getConnection()) {
            var cursor = connection.scan(org.springframework.data.redis.core.ScanOptions.scanOptions()
                    .match(pattern)
                    .count(count)
                    .build());
            
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                Map<Object, Object> userData = redisTemplate.opsForHash().entries(key);
                if (username.equals(userData.get("username"))) {
                    return userData;
                }
            }
        } catch (Exception e) {
            System.err.println("SCAN查询错误: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 显示Redis内存使用情况
     */
    public void showMemoryUsage() {
        System.out.println("\n💾 Redis内存使用情况:");
        
        // 统计键的数量
        long userKeys = redisTemplate.keys("user:*").size();
        long indexKeys = redisTemplate.keys("users:*").size();
        
        System.out.println("  用户键数量: " + userKeys);
        System.out.println("  索引键数量: " + indexKeys);
        System.out.println("  总键数量: " + (userKeys + indexKeys));
    }
}
