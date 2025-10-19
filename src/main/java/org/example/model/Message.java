package org.example.model;

import java.time.LocalDateTime;

/**
 * 消息模型
 */
public class Message {
    private String id;
    private String content;
    private String type;
    private LocalDateTime timestamp;
    private Integer priority;

    public Message() {
        this.timestamp = LocalDateTime.now();
    }

    public Message(String id, String content, String type) {
        this();
        this.id = id;
        this.content = content;
        this.type = type;
    }

    public Message(String id, String content, String type, Integer priority) {
        this(id, content, type);
        this.priority = priority;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", priority=" + priority +
                '}';
    }
}

