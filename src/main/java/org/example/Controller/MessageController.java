package org.example.controller;

import org.example.model.Message;
import org.example.service.MessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息控制器
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageProducerService messageProducerService;

    /**
     * 发送单个消息
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @RequestParam String content,
            @RequestParam String type,
            @RequestParam(required = false) Integer priority) {
        
        try {
            if (priority != null) {
                messageProducerService.sendMessage(content, type, priority);
            } else {
                messageProducerService.sendMessage(content, type);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "消息发送失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 发送消息对象
     */
    @PostMapping("/send-object")
    public ResponseEntity<Map<String, Object>> sendMessageObject(@RequestBody Message message) {
        try {
            messageProducerService.sendMessage(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "消息发送失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/send-batch")
    public ResponseEntity<Map<String, Object>> sendBatchMessages(
            @RequestParam int count,
            @RequestParam String type) {
        
        try {
            messageProducerService.sendBatchMessages(count, type);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量消息发送成功，共发送 " + count + " 条消息");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量消息发送失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取API信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "RabbitMQ Message Service");
        info.put("version", "1.0.0");
        info.put("endpoints", new String[]{
            "POST /api/messages/send - 发送单个消息",
            "POST /api/messages/send-object - 发送消息对象",
            "POST /api/messages/send-batch - 批量发送消息",
            "GET /api/messages/info - 获取API信息"
        });
        return ResponseEntity.ok(info);
    }
}

