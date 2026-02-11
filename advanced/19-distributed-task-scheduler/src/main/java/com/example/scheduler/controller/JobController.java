package com.example.scheduler.controller;

import com.example.scheduler.job.SimpleJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleJob(@RequestParam(defaultValue = "test-job") String name, 
                                              @RequestParam(defaultValue = "5") int delaySeconds) {
        try {
            String jobName = name + "-" + UUID.randomUUID().toString();
            String group = "DEFAULT";

            JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                    .withIdentity(jobName, group)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName + "-trigger", group)
                    .startAt(Date.from(LocalDateTime.now().plusSeconds(delaySeconds).atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return ResponseEntity.ok("Job scheduled: " + jobName + " to run in " + delaySeconds + " seconds");
        } catch (SchedulerException e) {
            return ResponseEntity.internalServerError().body("Error scheduling job: " + e.getMessage());
        }
    }
}
