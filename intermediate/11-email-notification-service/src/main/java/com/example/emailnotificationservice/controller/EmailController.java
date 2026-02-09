package com.example.emailnotificationservice.controller;

import com.example.emailnotificationservice.dto.EmailRequest;
import com.example.emailnotificationservice.service.EmailProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailProducer emailProducer;

    public EmailController(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailProducer.sendEmailNotification(emailRequest);
        return ResponseEntity.ok("Email notification queued successfully.");
    }
}
