package com.fabiankaraben.saga.commandmodel;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String itemType;
    private Double price;
    private String currency;
    private String status;

    public OrderAggregate() {
        // Required by Axon
    }

    @CommandHandler
    public OrderAggregate(CoreApi.CreateOrderCommand command) {
        // Validate
        if (command.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        apply(new CoreApi.OrderCreatedEvent(
                command.getOrderId(),
                command.getItemType(),
                command.getPrice(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(CoreApi.OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.itemType = event.getItemType();
        this.price = event.getPrice();
        this.currency = event.getCurrency();
        this.status = "CREATED";
    }

    @CommandHandler
    public void handle(CoreApi.ConfirmOrderCommand command) {
        apply(new CoreApi.OrderConfirmedEvent(command.getOrderId()));
    }

    @EventSourcingHandler
    public void on(CoreApi.OrderConfirmedEvent event) {
        this.status = "CONFIRMED";
    }

    @CommandHandler
    public void handle(CoreApi.CancelOrderCommand command) {
        apply(new CoreApi.OrderCancelledEvent(command.getOrderId(), command.getReason()));
    }

    @EventSourcingHandler
    public void on(CoreApi.OrderCancelledEvent event) {
        this.status = "CANCELLED";
    }
}
