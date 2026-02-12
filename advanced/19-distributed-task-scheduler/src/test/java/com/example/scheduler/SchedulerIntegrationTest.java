package com.example.scheduler;

import com.example.scheduler.job.SimpleJob;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SchedulerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("scheduler_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static ConfigurableApplicationContext app1;
    private static ConfigurableApplicationContext app2;

    @BeforeAll
    public static void startApps() {
        System.setProperty("SPRING_DATASOURCE_URL", postgres.getJdbcUrl());
        System.setProperty("SPRING_DATASOURCE_USERNAME", postgres.getUsername());
        System.setProperty("SPRING_DATASOURCE_PASSWORD", postgres.getPassword());

        // Start two application instances
        app1 = new SpringApplicationBuilder(SchedulerApplication.class)
                .properties("server.port=0", "spring.quartz.properties.org.quartz.scheduler.instanceId=instance1")
                .run();

        app2 = new SpringApplicationBuilder(SchedulerApplication.class)
                .properties(
                        "server.port=0", 
                        "spring.quartz.properties.org.quartz.scheduler.instanceId=instance2",
                        "spring.quartz.jdbc.initialize-schema=never"
                )
                .run();
    }

    @AfterAll
    public static void stopApps() {
        if (app1 != null) app1.close();
        if (app2 != null) app2.close();
    }

    @Test
    public void testDistributedJobExecution() throws SchedulerException, InterruptedException {
        // Get scheduler from app1 and schedule a job
        Scheduler scheduler1 = app1.getBean(Scheduler.class);
        
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("distributed-job", "test-group")
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("distributed-trigger", "test-group")
                .startNow()
                .build();

        scheduler1.scheduleJob(jobDetail, trigger);

        // Wait for job execution
        // We expect exactly 1 execution across both nodes because they share the same DB and are clustered
        await().atMost(10, TimeUnit.SECONDS).until(() -> SimpleJob.executionCount.get() >= 1);

        // Give it a bit more time to ensure no double execution happens
        Thread.sleep(2000);

        assertEquals(1, SimpleJob.executionCount.get(), "Job should be executed exactly once");
    }
}
