package com.fabiankaraben.saga;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SagaIntegrationTest {

    @Autowired
    private CommandGateway commandGateway;

    // We'll use a simple event listener to capture events for assertion
    @Autowired
    private TestEventListener testEventListener;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EventStorageEngine eventStorageEngine() {
            return new InMemoryEventStorageEngine();
        }

        @Bean
        public TestEventListener testEventListener() {
            return new TestEventListener();
        }
    }

    public static class TestEventListener {
        private final List<Object> events = Collections.synchronizedList(new ArrayList<>());

        @EventHandler
        public void on(Object event) {
            events.add(event);
        }

        public List<Object> getEvents() {
            return events;
        }
        
        public void clear() {
            events.clear();
        }
    }

    @BeforeEach
    void setUp() {
        testEventListener.clear();
    }

    @Test
    void testHappyPath() {
        String orderId = UUID.randomUUID().toString();
        
        commandGateway.sendAndWait(new CoreApi.CreateOrderCommand(orderId, "Laptop", 800.0, "USD"));

        // Wait for Saga to complete the flow: Created -> PaymentProcessed -> ShippingPrepared -> OrderConfirmed
        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            testEventListener.getEvents().stream().anyMatch(e -> e instanceof CoreApi.OrderConfirmedEvent)
        );

        List<Object> events = testEventListener.getEvents();
        
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderCreatedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.PaymentProcessedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.ShippingPreparedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderConfirmedEvent));
    }

    @Test
    void testPaymentFailureCompensation() {
        String orderId = UUID.randomUUID().toString();
        
        // Price > 1000 triggers payment failure
        commandGateway.sendAndWait(new CoreApi.CreateOrderCommand(orderId, "Gaming PC", 1500.0, "USD"));

        // Wait for Saga to compensate: Created -> PaymentFailed -> OrderCancelled
        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            testEventListener.getEvents().stream().anyMatch(e -> e instanceof CoreApi.OrderCancelledEvent)
        );

        List<Object> events = testEventListener.getEvents();
        
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderCreatedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.PaymentFailedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderCancelledEvent));
        
        // Ensure shipping was never prepared
        Assertions.assertFalse(events.stream().anyMatch(e -> e instanceof CoreApi.ShippingPreparedEvent));
    }

    @Test
    void testShippingFailureCompensation() {
        // OrderId containing "fail-shipping" triggers shipping failure
        String orderId = "fail-shipping-" + UUID.randomUUID().toString();
        
        commandGateway.sendAndWait(new CoreApi.CreateOrderCommand(orderId, "Laptop", 800.0, "USD"));

        // Wait for Saga to compensate: Created -> PaymentProcessed -> ShippingFailed -> PaymentCancelled -> OrderCancelled
        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            testEventListener.getEvents().stream().anyMatch(e -> e instanceof CoreApi.OrderCancelledEvent)
        );

        List<Object> events = testEventListener.getEvents();
        
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderCreatedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.PaymentProcessedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.ShippingFailedEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.PaymentCancelledEvent));
        Assertions.assertTrue(events.stream().anyMatch(e -> e instanceof CoreApi.OrderCancelledEvent));
    }
}
