package com.techbank.cqrscore.handlers;

import com.techbank.cqrscore.domain.AggregateRoot;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregate);
    T getById(String id);
}
