package com.techbank.accountcommand.api.commands;

import com.techbank.accountcommon.dto.AccountType;
import com.techbank.cqrscore.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolderName;
    private AccountType accountType;
    private double openingBalance;
}
