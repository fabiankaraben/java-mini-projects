package com.example.eventsourcing.gui;

import com.example.eventsourcing.core.api.CreateAccountCommand;
import com.example.eventsourcing.core.api.DepositMoneyCommand;
import com.example.eventsourcing.core.api.FindAccountQuery;
import com.example.eventsourcing.core.api.WithdrawMoneyCommand;
import com.example.eventsourcing.query.AccountSummary;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public AccountController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody BigDecimal initialBalance) {
        String accountId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateAccountCommand(accountId, initialBalance))
                .thenApply(result -> accountId);
    }

    @PutMapping("/{accountId}/deposit")
    public CompletableFuture<Void> deposit(@PathVariable String accountId, @RequestBody BigDecimal amount) {
        return commandGateway.send(new DepositMoneyCommand(accountId, amount));
    }

    @PutMapping("/{accountId}/withdraw")
    public CompletableFuture<Void> withdraw(@PathVariable String accountId, @RequestBody BigDecimal amount) {
        return commandGateway.send(new WithdrawMoneyCommand(accountId, amount));
    }

    @GetMapping("/{accountId}")
    public CompletableFuture<AccountSummary> getAccount(@PathVariable String accountId) {
        return queryGateway.query(new FindAccountQuery(accountId), ResponseTypes.instanceOf(AccountSummary.class));
    }
}
