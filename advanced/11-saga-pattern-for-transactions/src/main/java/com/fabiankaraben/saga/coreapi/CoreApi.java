package com.fabiankaraben.saga.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CoreApi {

    // Commands
    public static class CreateOrderCommand {
        @TargetAggregateIdentifier
        private final String orderId;
        private final String itemType;
        private final Double price;
        private final String currency;

        public CreateOrderCommand(String orderId, String itemType, Double price, String currency) {
            this.orderId = orderId;
            this.itemType = itemType;
            this.price = price;
            this.currency = currency;
        }

        public String getOrderId() { return orderId; }
        public String getItemType() { return itemType; }
        public Double getPrice() { return price; }
        public String getCurrency() { return currency; }
    }

    public static class ProcessPaymentCommand {
        @TargetAggregateIdentifier
        private final String paymentId;
        private final String orderId;
        private final Double amount;

        public ProcessPaymentCommand(String paymentId, String orderId, Double amount) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.amount = amount;
        }

        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
        public Double getAmount() { return amount; }
    }

    public static class CancelPaymentCommand {
        @TargetAggregateIdentifier
        private final String paymentId;
        private final String orderId;

        public CancelPaymentCommand(String paymentId, String orderId) {
            this.paymentId = paymentId;
            this.orderId = orderId;
        }

        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
    }

    public static class PrepareShippingCommand {
        @TargetAggregateIdentifier
        private final String shippingId;
        private final String orderId;
        private final String paymentId;

        public PrepareShippingCommand(String shippingId, String orderId, String paymentId) {
            this.shippingId = shippingId;
            this.orderId = orderId;
            this.paymentId = paymentId;
        }

        public String getShippingId() { return shippingId; }
        public String getOrderId() { return orderId; }
        public String getPaymentId() { return paymentId; }
    }

    public static class CancelOrderCommand {
        @TargetAggregateIdentifier
        private final String orderId;
        private final String reason;

        public CancelOrderCommand(String orderId, String reason) {
            this.orderId = orderId;
            this.reason = reason;
        }

        public String getOrderId() { return orderId; }
        public String getReason() { return reason; }
    }

    public static class ConfirmOrderCommand {
        @TargetAggregateIdentifier
        private final String orderId;

        public ConfirmOrderCommand(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderId() { return orderId; }
    }

    // Events
    public static class OrderCreatedEvent {
        private final String orderId;
        private final String itemType;
        private final Double price;
        private final String currency;

        public OrderCreatedEvent(String orderId, String itemType, Double price, String currency) {
            this.orderId = orderId;
            this.itemType = itemType;
            this.price = price;
            this.currency = currency;
        }

        public String getOrderId() { return orderId; }
        public String getItemType() { return itemType; }
        public Double getPrice() { return price; }
        public String getCurrency() { return currency; }
    }

    public static class PaymentProcessedEvent {
        private final String paymentId;
        private final String orderId;
        private final Double amount;

        public PaymentProcessedEvent(String paymentId, String orderId, Double amount) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.amount = amount;
        }

        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
        public Double getAmount() { return amount; }
    }

    public static class PaymentFailedEvent {
        private final String paymentId;
        private final String orderId;
        private final String reason;

        public PaymentFailedEvent(String paymentId, String orderId, String reason) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.reason = reason;
        }

        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
        public String getReason() { return reason; }
    }

    public static class PaymentCancelledEvent {
        private final String paymentId;
        private final String orderId;

        public PaymentCancelledEvent(String paymentId, String orderId) {
            this.paymentId = paymentId;
            this.orderId = orderId;
        }

        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
    }

    public static class ShippingPreparedEvent {
        private final String shippingId;
        private final String orderId;
        private final String paymentId;

        public ShippingPreparedEvent(String shippingId, String orderId, String paymentId) {
            this.shippingId = shippingId;
            this.orderId = orderId;
            this.paymentId = paymentId;
        }

        public String getShippingId() { return shippingId; }
        public String getOrderId() { return orderId; }
        public String getPaymentId() { return paymentId; }
    }

    public static class ShippingFailedEvent {
        private final String shippingId;
        private final String orderId;
        private final String reason;

        public ShippingFailedEvent(String shippingId, String orderId, String reason) {
            this.shippingId = shippingId;
            this.orderId = orderId;
            this.reason = reason;
        }

        public String getShippingId() { return shippingId; }
        public String getOrderId() { return orderId; }
        public String getReason() { return reason; }
    }

    public static class OrderConfirmedEvent {
        private final String orderId;

        public OrderConfirmedEvent(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderId() { return orderId; }
    }

    public static class OrderCancelledEvent {
        private final String orderId;
        private final String reason;

        public OrderCancelledEvent(String orderId, String reason) {
            this.orderId = orderId;
            this.reason = reason;
        }

        public String getOrderId() { return orderId; }
        public String getReason() { return reason; }
    }
}
