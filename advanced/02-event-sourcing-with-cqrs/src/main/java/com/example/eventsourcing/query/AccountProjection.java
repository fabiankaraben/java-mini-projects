package com.example.eventsourcing.query;

import com.example.eventsourcing.core.api.AccountCreatedEvent;
import com.example.eventsourcing.core.api.FindAccountQuery;
import com.example.eventsourcing.core.api.MoneyDepositedEvent;
import com.example.eventsourcing.core.api.MoneyWithdrawnEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class AccountProjection {

    private final AccountSummaryRepository repository;

    public AccountProjection(AccountSummaryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        repository.save(new AccountSummary(event.accountId(), event.initialBalance()));
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) {
        repository.findById(event.accountId()).ifPresent(account -> {
            account.setBalance(account.getBalance().add(event.amount()));
            repository.save(account);
        });
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event) {
        repository.findById(event.accountId()).ifPresent(account -> {
            account.setBalance(account.getBalance().subtract(event.amount()));
            repository.save(account);
        });
    }

    @QueryHandler
    public AccountSummary handle(FindAccountQuery query) {
        return repository.findById(query.accountId()).orElse(null);
    }
}
