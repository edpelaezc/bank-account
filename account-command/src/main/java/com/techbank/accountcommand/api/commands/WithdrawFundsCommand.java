package com.techbank.accountcommand.api.commands;

import com.techbank.cqrscore.commands.BaseCommand;
import lombok.Data;

@Data
public class WithdrawFundsCommand extends BaseCommand {
    private double amount;
}
