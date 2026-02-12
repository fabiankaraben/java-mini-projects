package com.example.hft.model;

public class TradeEvent {
    private String symbol;
    private double price;
    private double volume;
    private long timestamp;
    private String orderId;

    public void set(String symbol, double price, double volume, long timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
        this.orderId = null; // Reset for reuse
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "TradeEvent{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                ", timestamp=" + timestamp +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
