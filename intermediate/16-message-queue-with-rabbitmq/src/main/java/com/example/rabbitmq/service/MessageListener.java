package com.example.rabbitmq.service;

import com.example.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageListener {

    private final List<String> receivedMessages = new ArrayList<>();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        synchronized (receivedMessages) {
            receivedMessages.add(message);
        }
    }

    public List<String> getReceivedMessages() {
        synchronized (receivedMessages) {
            return new ArrayList<>(receivedMessages);
        }
    }
    
    public void clearMessages() {
        synchronized (receivedMessages) {
            receivedMessages.clear();
        }
    }
}
