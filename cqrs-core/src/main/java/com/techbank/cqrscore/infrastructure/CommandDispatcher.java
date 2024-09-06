package com.techbank.cqrscore.infrastructure;

import com.techbank.cqrscore.commands.BaseCommand;
import com.techbank.cqrscore.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
