package com.example.emailnotificationservice.service;

import com.example.emailnotificationservice.config.RabbitMQConfig;
import com.example.emailnotificationservice.dto.EmailRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final JavaMailSender javaMailSender;

    public EmailConsumer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.to());
        message.setSubject(emailRequest.subject());
        message.setText(emailRequest.body());
        
        // In a real scenario, we might want to set a "From" address.
        // For this example/test, it can be configured in properties or left default if supported.
        message.setFrom("noreply@example.com");

        javaMailSender.send(message);
        System.out.println("Email sent to: " + emailRequest.to());
    }
}
