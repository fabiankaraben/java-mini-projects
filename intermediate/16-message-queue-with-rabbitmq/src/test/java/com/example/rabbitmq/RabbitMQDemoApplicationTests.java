package com.example.rabbitmq;

import com.example.rabbitmq.service.MessageListener;
import com.example.rabbitmq.service.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class RabbitMQDemoApplicationTests {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private MessageListener messageListener;

    @Test
    void shouldSendAndReceiveMessage() {
        String message = "Hello Testcontainers!";
        
        messagePublisher.sendMessage(message);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            assertThat(messageListener.getReceivedMessages()).contains(message);
        });
    }
}
