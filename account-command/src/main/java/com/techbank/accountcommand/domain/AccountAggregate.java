package com.techbank.accountcommand.domain;

import com.techbank.accountcommand.api.commands.OpenAccountCommand;
import com.techbank.accountcommon.events.AccountClosedEvent;
import com.techbank.accountcommon.events.AccountOpenedEvent;
import com.techbank.accountcommon.events.FundsDepositedEvent;
import com.techbank.accountcommon.events.FundsWithdrawnEvent;
import com.techbank.cqrscore.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolderName(command.getAccountHolderName())
                .createdAt(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Cannot deposit funds to a closed account");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Cannot withdraw funds from a closed account");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("The bank account has already been closed!");
        }

        raiseEvent(AccountClosedEvent.builder()
                    .id(this.id)
                    .build());
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }
}
