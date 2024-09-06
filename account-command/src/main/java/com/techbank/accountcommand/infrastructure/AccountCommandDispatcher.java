package com.techbank.accountcommand.infrastructure;

import com.techbank.cqrscore.commands.BaseCommand;
import com.techbank.cqrscore.commands.CommandHandlerMethod;
import com.techbank.cqrscore.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, k -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No handler registered for command " + command.getClass());
        }

        if (handlers.size() > 1) {
            throw new RuntimeException("Multiple handler registered for command " + command.getClass());
        }
        handlers.get(0).handle(command);
    }
}
