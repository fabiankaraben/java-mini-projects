package com.example.workflow.model;

import java.util.List;
import java.util.ArrayList;

public class Workflow {
    private String id;
    private List<Task> tasks = new ArrayList<>();
    private WorkflowStatus status = WorkflowStatus.PENDING;

    public Workflow() {}

    public Workflow(String id, List<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }
}
