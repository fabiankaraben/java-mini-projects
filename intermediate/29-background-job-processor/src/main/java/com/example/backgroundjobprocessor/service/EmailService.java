package com.example.backgroundjobprocessor.service;

import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Job(name = "The sample job with variable %0")
    public void sendEmail(String recipient) {
        logger.info("Sending email to {} at {}", recipient, Instant.now());
        try {
            Thread.sleep(1000); // Simulate some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("Email sent to {}", recipient);
    }
}
