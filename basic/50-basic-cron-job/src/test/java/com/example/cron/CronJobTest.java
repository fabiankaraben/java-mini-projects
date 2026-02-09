package com.example.cron;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CronJobTest {

    @Test
    void testTaskExecution() throws InterruptedException {
        CronJob cronJob = new CronJob();
        int expectedExecutions = 3;
        CountDownLatch latch = new CountDownLatch(expectedExecutions);
        AtomicInteger counter = new AtomicInteger(0);

        Runnable task = () -> {
            System.out.println("Test task running. Count: " + counter.incrementAndGet());
            latch.countDown();
        };

        // Schedule task to run every 100ms
        cronJob.start(task, 0, 100, TimeUnit.MILLISECONDS);

        // Wait for task to run 3 times, with a timeout of 2 seconds
        boolean completed = latch.await(2, TimeUnit.SECONDS);
        
        cronJob.stop();

        assertTrue(completed, "Task should execute 3 times within timeout");
        assertTrue(counter.get() >= expectedExecutions, "Counter should be at least 3");
    }
}
