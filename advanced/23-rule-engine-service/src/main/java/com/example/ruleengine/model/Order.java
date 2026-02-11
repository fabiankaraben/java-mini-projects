package com.example.ruleengine.model;

public class Order {
    private String orderId;
    private String customerType;
    private double amount;
    private double discount;
    private String ruleApplied;

    public Order() {
    }

    public Order(String orderId, String customerType, double amount, double discount, String ruleApplied) {
        this.orderId = orderId;
        this.customerType = customerType;
        this.amount = amount;
        this.discount = discount;
        this.ruleApplied = ruleApplied;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getRuleApplied() {
        return ruleApplied;
    }

    public void setRuleApplied(String ruleApplied) {
        this.ruleApplied = ruleApplied;
    }
}
