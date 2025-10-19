package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    // 队列名称
    public static final String QUEUE_NAME = "message.queue";
    public static final String EXCHANGE_NAME = "message.exchange";
    public static final String ROUTING_KEY = "message.routing.key";

    /**
     * 声明队列
     */
    @Bean
    public Queue messageQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange messageExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding messageBinding() {
        return BindingBuilder
                .bind(messageQueue())
                .to(messageExchange())
                .with(ROUTING_KEY);
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    /**
     * 配置消息监听器容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置并发消费者数量
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
}

