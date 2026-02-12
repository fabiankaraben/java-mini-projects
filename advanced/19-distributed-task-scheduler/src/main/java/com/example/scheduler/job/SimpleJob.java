package com.example.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimpleJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class);
    
    // Static counter to verify execution count in tests (simplistic approach for demo)
    public static final AtomicInteger executionCount = new AtomicInteger(0);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        String instanceId;
        try {
            instanceId = context.getScheduler().getSchedulerInstanceId();
        } catch (SchedulerException e) {
            instanceId = "unknown";
        }
        logger.info("Executing job: {} on instance: {}", jobName, instanceId);
        executionCount.incrementAndGet();
        try {
            // Simulate some work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("Finished job: {}", jobName);
    }
}
