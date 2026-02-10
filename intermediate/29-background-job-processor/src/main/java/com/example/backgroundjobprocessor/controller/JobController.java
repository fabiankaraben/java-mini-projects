package com.example.backgroundjobprocessor.controller;

import com.example.backgroundjobprocessor.service.EmailService;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class JobController {

    private final JobScheduler jobScheduler;
    private final EmailService emailService;

    public JobController(JobScheduler jobScheduler, EmailService emailService) {
        this.jobScheduler = jobScheduler;
        this.emailService = emailService;
    }

    @PostMapping("/jobs/email")
    public String scheduleEmailJob(@RequestParam String recipient) {
        jobScheduler.enqueue(() -> emailService.sendEmail(recipient));
        return "Job enqueued for recipient: " + recipient;
    }
    
    @PostMapping("/jobs/email/delayed")
    public String scheduleDelayedEmailJob(@RequestParam String recipient) {
        jobScheduler.schedule(Instant.now().plusSeconds(10), () -> emailService.sendEmail(recipient));
        return "Delayed job scheduled for recipient: " + recipient;
    }
}
