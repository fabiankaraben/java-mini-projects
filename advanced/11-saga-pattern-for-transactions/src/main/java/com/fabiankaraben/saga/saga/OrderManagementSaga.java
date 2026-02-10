package com.fabiankaraben.saga.saga;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderManagementSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private String paymentId;
    private String shippingId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(CoreApi.OrderCreatedEvent event) {
        this.paymentId = UUID.randomUUID().toString();
        // Associate with paymentId so we can receive payment events
        SagaLifecycle.associateWith("paymentId", this.paymentId);
        
        System.out.println("Saga started for Order: " + event.getOrderId() + ". Sending ProcessPaymentCommand...");

        commandGateway.send(new CoreApi.ProcessPaymentCommand(
                this.paymentId, 
                event.getOrderId(), 
                event.getPrice()
        ));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(CoreApi.PaymentProcessedEvent event) {
        System.out.println("Payment processed for Order: " + event.getOrderId() + ". Sending PrepareShippingCommand...");
        
        this.shippingId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("shippingId", this.shippingId);

        commandGateway.send(new CoreApi.PrepareShippingCommand(
                this.shippingId, 
                event.getOrderId(), 
                event.getPaymentId()
        ));
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(CoreApi.ShippingPreparedEvent event) {
        System.out.println("Shipping prepared for Order: " + event.getOrderId() + ". Sending ConfirmOrderCommand...");
        
        commandGateway.send(new CoreApi.ConfirmOrderCommand(event.getOrderId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(CoreApi.OrderConfirmedEvent event) {
        System.out.println("Order confirmed: " + event.getOrderId() + ". Saga ended.");
    }

    // Compensations

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(CoreApi.PaymentFailedEvent event) {
        System.out.println("Payment failed for Order: " + event.getOrderId() + ". Cancelling Order...");
        commandGateway.send(new CoreApi.CancelOrderCommand(event.getOrderId(), "Payment Failed: " + event.getReason()));
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(CoreApi.ShippingFailedEvent event) {
        System.out.println("Shipping failed for Order: " + event.getOrderId() + ". Compensating Payment...");
        
        commandGateway.send(new CoreApi.CancelPaymentCommand(this.paymentId, event.getOrderId()));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(CoreApi.PaymentCancelledEvent event) {
        System.out.println("Payment cancelled for Order: " + event.getOrderId() + ". Cancelling Order...");
        
        commandGateway.send(new CoreApi.CancelOrderCommand(event.getOrderId(), "Shipping Failed -> Payment Refunded"));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(CoreApi.OrderCancelledEvent event) {
        System.out.println("Order cancelled: " + event.getOrderId() + ". Reason: " + event.getReason() + ". Saga ended.");
    }
}
