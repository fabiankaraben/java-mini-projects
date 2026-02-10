package com.example.eventsourcing.core.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import java.math.BigDecimal;

public record CreateAccountCommand(@TargetAggregateIdentifier String accountId, BigDecimal initialBalance) {}
