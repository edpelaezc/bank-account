package com.techbank.accountcommand.api.commands;

import com.techbank.cqrscore.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String accountId) {
        super(accountId);
    }
}
