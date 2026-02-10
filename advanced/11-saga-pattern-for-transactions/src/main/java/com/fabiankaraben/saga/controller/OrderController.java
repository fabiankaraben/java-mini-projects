package com.fabiankaraben.saga.controller;

import com.fabiankaraben.saga.coreapi.CoreApi;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CommandGateway commandGateway;

    public OrderController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createOrder(@RequestBody OrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new CoreApi.CreateOrderCommand(
                orderId,
                request.getItemType(),
                request.getPrice(),
                request.getCurrency()
        ));
    }

    public static class OrderRequest {
        private String itemType;
        private Double price;
        private String currency;

        public String getItemType() { return itemType; }
        public void setItemType(String itemType) { this.itemType = itemType; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
}
