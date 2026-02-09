package com.example.rabbitmq.controller;

import com.example.rabbitmq.service.MessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessagePublisher messagePublisher;

    public MessageController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        messagePublisher.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ: " + message);
    }
}
