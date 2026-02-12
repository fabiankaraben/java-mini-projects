package com.example.chaos.dummy;

public class DummyService {
    public String sayHello() {
        return "Hello World";
    }

    public void processData() {
        // Simulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
