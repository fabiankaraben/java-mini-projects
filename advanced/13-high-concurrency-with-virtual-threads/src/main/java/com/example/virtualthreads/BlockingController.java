package com.example.virtualthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockingController {

    private static final Logger logger = LoggerFactory.getLogger(BlockingController.class);

    @GetMapping("/block")
    public String block(@RequestParam(defaultValue = "1000") int delayMs) throws InterruptedException {
        // Simulate a blocking operation (e.g., DB call, external API)
        // With platform threads, this would block a valuable OS thread.
        // With virtual threads, the carrier thread is released.
        Thread.sleep(delayMs);
        return "Processed on " + Thread.currentThread();
    }
}
