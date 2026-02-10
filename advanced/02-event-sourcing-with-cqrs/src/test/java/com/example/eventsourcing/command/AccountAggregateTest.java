package com.example.eventsourcing.command;

import com.example.eventsourcing.core.api.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class AccountAggregateTest {

    private FixtureConfiguration<AccountAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(AccountAggregate.class);
    }

    @Test
    void shouldCreateAccount() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal initialBalance = BigDecimal.valueOf(100);

        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand(accountId, initialBalance))
                .expectEvents(new AccountCreatedEvent(accountId, initialBalance));
    }

    @Test
    void shouldDepositMoney() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal depositAmount = BigDecimal.valueOf(50);

        fixture.given(new AccountCreatedEvent(accountId, initialBalance))
                .when(new DepositMoneyCommand(accountId, depositAmount))
                .expectEvents(new MoneyDepositedEvent(accountId, depositAmount));
    }

    @Test
    void shouldWithdrawMoney() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);

        fixture.given(new AccountCreatedEvent(accountId, initialBalance))
                .when(new WithdrawMoneyCommand(accountId, withdrawAmount))
                .expectEvents(new MoneyWithdrawnEvent(accountId, withdrawAmount));
    }

    @Test
    void shouldNotWithdrawNegativeAmount() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.valueOf(-10);

        fixture.given(new AccountCreatedEvent(accountId, initialBalance))
                .when(new WithdrawMoneyCommand(accountId, withdrawAmount))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotWithdrawInsufficientFunds() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.valueOf(150);

        fixture.given(new AccountCreatedEvent(accountId, initialBalance))
                .when(new WithdrawMoneyCommand(accountId, withdrawAmount))
                .expectException(IllegalArgumentException.class);
    }
}
