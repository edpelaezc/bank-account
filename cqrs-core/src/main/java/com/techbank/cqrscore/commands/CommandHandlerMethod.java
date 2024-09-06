package com.techbank.cqrscore.commands;

public interface CommandHandlerMethod<T extends BaseCommand> {
    void handle(T command);
}
