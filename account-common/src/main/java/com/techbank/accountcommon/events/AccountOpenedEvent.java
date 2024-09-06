package com.techbank.accountcommon.events;

import com.techbank.accountcommon.dto.AccountType;
import com.techbank.cqrscore.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountOpenedEvent extends BaseEvent {
    private String accountHolderName;
    private AccountType accountType;
    private Date createdAt;
    private double openingBalance;
}
