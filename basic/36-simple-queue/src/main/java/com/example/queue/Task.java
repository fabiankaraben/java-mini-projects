package com.example.queue;

import java.util.UUID;

public class Task {
    private final String id;
    private final String content;
    private final long createdAt;

    public Task(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Task{id='" + id + "', content='" + content + "'}";
    }
}
