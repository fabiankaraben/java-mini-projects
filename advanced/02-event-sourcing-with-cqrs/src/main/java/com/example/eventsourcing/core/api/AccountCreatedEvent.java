package com.example.eventsourcing.core.api;

import java.math.BigDecimal;

public record AccountCreatedEvent(String accountId, BigDecimal initialBalance) {}
