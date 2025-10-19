package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class MySQLDataService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 从shiyan.json文件读取数据并插入到MySQL
     */
    public void insertDataFromFile() {
        try {
            // 读取JSON文件
            JsonNode rootNode = objectMapper.readTree(new File("shiyan.json"));
            
            long startTime = System.currentTimeMillis();
            
            // 清空现有数据
            clearTables();
            
            // 插入用户数据
            insertUsers(rootNode.get("users"));
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("✅ MySQL数据插入完成！");
            System.out.println("📊 总耗时: " + (endTime - startTime) + " 毫秒");
            
        } catch (IOException e) {
            System.err.println("❌ 读取数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 清空表数据
     */
    private void clearTables() {
        try {
            // 清空用户表
            jdbcTemplate.execute("TRUNCATE TABLE users");
            
            System.out.println("🗑️ 用户表数据已清空");
        } catch (Exception e) {
            System.err.println("❌ 清空表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 插入用户数据
     */
    private void insertUsers(JsonNode usersNode) {
        String sql = "INSERT INTO users (username, email, age, city, balance, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        for (JsonNode user : usersNode) {
            jdbcTemplate.update(sql,
                user.get("username").asText(),
                user.get("email").asText(),
                user.get("age").asInt(),
                user.get("city").asText(),
                new BigDecimal(user.get("balance").asText()),
                user.get("created_at").asText()
            );
        }
        
        System.out.println("📥 用户数据插入完成: " + usersNode.size() + " 条");
    }
    
    
    /**
     * 测试MySQL查询性能 - 根据username查询user
     */
    public void testMySQLPerformance() {
        System.out.println("\n🔍 开始MySQL性能测试...");
        
        // 测试根据username查询user的性能
        String testUsername = "user_0001";
        String sql = "SELECT * FROM users WHERE username = ?";
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 根据username查询user
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, testUsername);
            
            long endTime = System.currentTimeMillis();
            long queryTime = endTime - startTime;
            
            System.out.println("  MySQL查询用户 (username=" + testUsername + "): " + queryTime + "ms");
            System.out.println("  查询结果: " + (result.isEmpty() ? "未找到" : "找到 " + result.size() + " 条记录"));
        } catch (Exception e) {
            System.out.println("MySQL查询错误: " + e.getMessage());
        }
    }
}
