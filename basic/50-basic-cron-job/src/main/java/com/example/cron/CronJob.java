package com.example.cron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CronJob {

    private final ScheduledExecutorService scheduler;

    public CronJob() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start(Runnable task, long initialDelay, long period, TimeUnit unit) {
        System.out.println("Starting cron job...");
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public void stop() {
        System.out.println("Stopping cron job...");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    public static void main(String[] args) {
        CronJob cronJob = new CronJob();
        
        Runnable task = () -> System.out.println("Task executed at: " + System.currentTimeMillis());

        // Run every 2 seconds
        cronJob.start(task, 0, 2, TimeUnit.SECONDS);

        // Keep running for 10 seconds then exit
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        cronJob.stop();
    }
}
