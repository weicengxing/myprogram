package org.example.service;

import org.example.config.RabbitMQConfig;
import org.example.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 消息生产者服务
 */
@Service
public class MessageProducerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducerService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    public void sendMessage(String content, String type) {
        Message message = new Message(UUID.randomUUID().toString(), content, type);
        sendMessage(message);
    }

    /**
     * 发送消息
     */
    public void sendMessage(String content, String type, Integer priority) {
        Message message = new Message(UUID.randomUUID().toString(), content, type, priority);
        sendMessage(message);
    }

    /**
     * 发送消息对象
     */
    public void sendMessage(Message message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
            logger.info("消息发送成功: {}", message);
        } catch (Exception e) {
            logger.error("消息发送失败: {}", message, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     * 批量发送消息
     */
    public void sendBatchMessages(int count, String type) {
        for (int i = 0; i < count; i++) {
            sendMessage("批量消息 " + (i + 1), type, i % 3 + 1);
        }
        logger.info("批量发送了 {} 条消息", count);
    }
}

