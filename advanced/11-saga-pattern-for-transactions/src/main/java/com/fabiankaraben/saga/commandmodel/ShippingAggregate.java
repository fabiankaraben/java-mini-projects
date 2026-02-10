package com.fabiankaraben.saga.commandmodel;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ShippingAggregate {

    @AggregateIdentifier
    private String shippingId;
    private String orderId;
    private String paymentId;
    private String status;

    public ShippingAggregate() {
        // Required by Axon
    }

    @CommandHandler
    public ShippingAggregate(CoreApi.PrepareShippingCommand command) {
        if (command.getOrderId().contains("fail-shipping")) {
             apply(new CoreApi.ShippingFailedEvent(
                    command.getShippingId(),
                    command.getOrderId(),
                    "Shipping unavailable for this order"
             ));
             return;
        }

        apply(new CoreApi.ShippingPreparedEvent(
                command.getShippingId(),
                command.getOrderId(),
                command.getPaymentId()
        ));
    }

    @EventSourcingHandler
    public void on(CoreApi.ShippingPreparedEvent event) {
        this.shippingId = event.getShippingId();
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
        this.status = "PREPARED";
    }

    @EventSourcingHandler
    public void on(CoreApi.ShippingFailedEvent event) {
        this.shippingId = event.getShippingId();
        this.orderId = event.getOrderId();
        this.status = "FAILED";
    }
}
