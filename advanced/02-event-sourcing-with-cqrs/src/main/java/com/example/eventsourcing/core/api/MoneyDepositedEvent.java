package com.example.eventsourcing.core.api;

import java.math.BigDecimal;

public record MoneyDepositedEvent(String accountId, BigDecimal amount) {}
