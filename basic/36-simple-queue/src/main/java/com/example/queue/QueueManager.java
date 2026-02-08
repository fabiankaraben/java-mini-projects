package com.example.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager {
    private final BlockingQueue<Task> queue;

    public QueueManager() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void submit(Task task) {
        queue.offer(task);
    }

    public Task take() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }
}
