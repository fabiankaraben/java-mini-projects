package com.example.workflow.model;

import java.util.HashSet;
import java.util.Set;

public class Task {
    private String id;
    private String type; // e.g., "LOG", "WAIT"
    private String payload; // simple payload for execution
    private Set<String> dependencies = new HashSet<>();
    private TaskStatus status = TaskStatus.PENDING;

    public Task() {}

    public Task(String id, String type, String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<String> dependencies) {
        this.dependencies = dependencies;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
