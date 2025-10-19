# RabbitMQ 消息队列集成

本项目已成功集成RabbitMQ消息队列，并使用线程池进行异步消息处理。

## 功能特性

- ✅ RabbitMQ消息队列集成
- ✅ 线程池异步消息处理
- ✅ 多种消息类型支持（EMAIL、SMS、NOTIFICATION）
- ✅ 消息优先级处理
- ✅ 批量消息发送
- ✅ RESTful API接口

## 项目结构

```
src/main/java/org/example/
├── config/
│   ├── RabbitMQConfig.java          # RabbitMQ配置
│   └── ThreadPoolConfig.java        # 线程池配置
├── controller/
│   └── MessageController.java       # 消息API控制器
├── model/
│   └── Message.java                 # 消息模型
├── service/
│   ├── MessageProducerService.java  # 消息生产者服务
│   └── MessageConsumerService.java  # 消息消费者服务
└── MyprogramApplication.java        # 主应用类
```

## 配置说明

### RabbitMQ配置
- 主机: localhost
- 端口: 5672
- 用户名: guest
- 密码: guest
- 虚拟主机: /

### 线程池配置
- 核心线程数: 5
- 最大线程数: 20
- 队列容量: 200
- 线程空闲时间: 60秒

## API接口

### 1. 发送单个消息
```http
POST /api/messages/send
Content-Type: application/x-www-form-urlencoded

content=测试消息&type=EMAIL&priority=1
```

### 2. 发送消息对象
```http
POST /api/messages/send-object
Content-Type: application/json

{
    "id": "msg-001",
    "content": "测试消息",
    "type": "SMS",
    "priority": 2
}
```

### 3. 批量发送消息
```http
POST /api/messages/send-batch
Content-Type: application/x-www-form-urlencoded

count=10&type=NOTIFICATION
```

### 4. 获取API信息
```http
GET /api/messages/info
```

## 运行前准备

1. 确保RabbitMQ服务已启动
2. 确保Redis服务已启动（如果使用Redis功能）
3. 运行应用：`mvn spring-boot:run`

## 测试示例

### 使用curl测试

1. 发送单个消息：
```bash
curl -X POST "http://localhost:8080/api/messages/send" \
  -d "content=Hello World&type=EMAIL&priority=1"
```

2. 发送消息对象：
```bash
curl -X POST "http://localhost:8080/api/messages/send-object" \
  -H "Content-Type: application/json" \
  -d '{"content":"测试消息","type":"SMS","priority":2}'
```

3. 批量发送消息：
```bash
curl -X POST "http://localhost:8080/api/messages/send-batch" \
  -d "count=5&type=NOTIFICATION"
```

## 消息处理流程

1. 消息通过REST API发送到生产者服务
2. 生产者将消息发送到RabbitMQ队列
3. 消费者监听队列并接收消息
4. 消息被异步提交到线程池进行处理
5. 根据消息类型执行相应的业务逻辑

## 监控和日志

应用会输出详细的日志信息，包括：
- 消息发送状态
- 消息接收状态
- 线程池处理状态
- 错误信息

查看控制台输出可以监控消息处理情况。

