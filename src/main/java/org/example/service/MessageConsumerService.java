package org.example.service;

import org.example.config.RabbitMQConfig;
import org.example.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 消息消费者服务
 */
@Service
public class MessageConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);

    @Autowired
    @Qualifier("messageTaskExecutor")
    private Executor messageTaskExecutor;

    /**
     * 监听消息队列
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Message message) {
        logger.info("接收到消息: {}", message);
        
        // 使用线程池异步处理消息
        processMessageAsync(message);
    }

    /**
     * 异步处理消息
     */
    @Async("messageTaskExecutor")
    public CompletableFuture<Void> processMessageAsync(Message message) {
        try {
            logger.info("开始处理消息 [{}] 在线程: {}", message.getId(), Thread.currentThread().getName());
            
            // 模拟消息处理逻辑
            processMessage(message);
            
            logger.info("消息处理完成 [{}]", message.getId());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("消息处理失败 [{}]: {}", message.getId(), e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 处理消息的具体业务逻辑
     */
    private void processMessage(Message message) {
        try {
            // 根据消息类型进行不同的处理
            switch (message.getType()) {
                case "EMAIL":
                    processEmailMessage(message);
                    break;
                case "SMS":
                    processSmsMessage(message);
                    break;
                case "NOTIFICATION":
                    processNotificationMessage(message);
                    break;
                default:
                    processDefaultMessage(message);
                    break;
            }
            
            // 模拟处理时间
            Thread.sleep(1000 + (message.getPriority() != null ? message.getPriority() * 200 : 0));
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("消息处理被中断", e);
        }
    }

    /**
     * 处理邮件消息
     */
    private void processEmailMessage(Message message) {
        logger.info("处理邮件消息: {}", message.getContent());
        // 这里可以添加发送邮件的逻辑
    }

    /**
     * 处理短信消息
     */
    private void processSmsMessage(Message message) {
        logger.info("处理短信消息: {}", message.getContent());
        // 这里可以添加发送短信的逻辑
    }

    /**
     * 处理通知消息
     */
    private void processNotificationMessage(Message message) {
        logger.info("处理通知消息: {}", message.getContent());
        // 这里可以添加发送通知的逻辑
    }

    /**
     * 处理默认消息
     */
    private void processDefaultMessage(Message message) {
        logger.info("处理默认消息: {}", message.getContent());
        // 这里可以添加默认处理逻辑
    }
}

