package com.example.chaos.demo;

import com.example.chaos.dummy.DummyService;

public class DummyApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Dummy App...");
        DummyService service = new DummyService();
        
        while (true) {
            try {
                System.out.println(service.sayHello());
                service.processData();
            } catch (Exception e) {
                System.err.println("Caught exception: " + e.getMessage());
            }
            Thread.sleep(1000);
        }
    }
}
