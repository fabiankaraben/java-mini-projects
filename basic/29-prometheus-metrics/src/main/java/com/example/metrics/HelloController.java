package com.example.metrics;

import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {

    private final Random random = new Random();

    @GetMapping("/hello")
    @Timed(value = "hello.request", description = "Time taken to return hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/random")
    @Timed(value = "random.request", description = "Time taken to return a random number")
    public String random() throws InterruptedException {
        // Simulate some latency
        Thread.sleep(random.nextInt(200));
        return "Random: " + random.nextInt(100);
    }
}
