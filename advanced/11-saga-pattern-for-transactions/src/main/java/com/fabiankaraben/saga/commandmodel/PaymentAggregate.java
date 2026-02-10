package com.fabiankaraben.saga.commandmodel;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private Double amount;
    private String status;

    public PaymentAggregate() {
        // Required by Axon
    }

    @CommandHandler
    public PaymentAggregate(CoreApi.ProcessPaymentCommand command) {
        if (command.getAmount() > 1000) {
            apply(new CoreApi.PaymentFailedEvent(
                    command.getPaymentId(),
                    command.getOrderId(),
                    "Payment limit exceeded"
            ));
            return;
        }
        
        apply(new CoreApi.PaymentProcessedEvent(
                command.getPaymentId(),
                command.getOrderId(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(CoreApi.PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
        this.amount = event.getAmount();
        this.status = "PROCESSED";
    }

    @EventSourcingHandler
    public void on(CoreApi.PaymentFailedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
        this.status = "FAILED";
    }

    @CommandHandler
    public void handle(CoreApi.CancelPaymentCommand command) {
        if (!"FAILED".equals(this.status)) {
             apply(new CoreApi.PaymentCancelledEvent(command.getPaymentId(), command.getOrderId()));
        }
    }

    @EventSourcingHandler
    public void on(CoreApi.PaymentCancelledEvent event) {
        this.status = "CANCELLED";
    }
}
