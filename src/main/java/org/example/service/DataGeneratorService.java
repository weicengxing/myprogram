package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DataGeneratorService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    
    // 城市列表
    private final String[] cities = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "成都", "西安", "重庆"};
    
    // 商品分类
    private final String[] categories = {"电子产品", "服装鞋帽", "家居用品", "食品饮料", "图书音像", "运动户外", "美妆护肤", "母婴用品"};
    
    // 商品名称前缀
    private final String[] productPrefixes = {"智能", "时尚", "经典", "高端", "实用", "精美", "优质", "创新"};
    
    // 商品名称后缀
    private final String[] productSuffixes = {"手机", "电脑", "耳机", "手表", "包包", "鞋子", "衣服", "家具", "食品", "书籍"};
    
    // 订单状态
    private final String[] orderStatuses = {"PENDING", "PAID", "SHIPPED", "DELIVERED", "CANCELLED"};
    
    /**
     * 生成测试数据并保存到shiyan.json文件
     */
    public void generateTestData() {
        try {
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> users = new ArrayList<>();
            
            // 生成10000个用户
            for (int i = 1; i <= 10000; i++) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", i);
                user.put("username", "user_" + String.format("%04d", i));
                user.put("email", "user" + i + "@example.com");
                user.put("age", random.nextInt(50) + 18); // 18-67岁
                user.put("city", cities[random.nextInt(cities.length)]);
                user.put("balance", new BigDecimal(random.nextDouble() * 10000).setScale(2, BigDecimal.ROUND_HALF_UP));
                user.put("created_at", LocalDateTime.now().minusDays(random.nextInt(365)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                users.add(user);
            }
            
            data.put("users", users);
            data.put("generated_at", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.put("total_users", users.size());
//
            // 保存到文件
              try (FileWriter writer = new FileWriter("shiyan.json", java.nio.charset.StandardCharsets.UTF_8)) {
                  objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, data);
              }

            System.out.println("✅ 测试数据生成完成！");
            System.out.println("📊 用户数据: " + users.size() + " 条");
            System.out.println("📁 文件保存: shiyan.json");
            
        }
        catch (Exception e) //IO
        {
            System.err.println("❌ 数据生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
