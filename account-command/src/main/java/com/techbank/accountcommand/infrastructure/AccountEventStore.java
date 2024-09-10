package com.techbank.accountcommand.infrastructure;

import com.techbank.accountcommand.domain.AccountAggregate;
import com.techbank.accountcommand.domain.EventStoreRepository;
import com.techbank.cqrscore.events.BaseEvent;
import com.techbank.cqrscore.events.EventModel;
import com.techbank.cqrscore.exceptions.AggregateNotFoundException;
import com.techbank.cqrscore.exceptions.ConcurrencyException;
import com.techbank.cqrscore.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {
    private final EventStoreRepository repository;

    public AccountEventStore(EventStoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = repository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }

        var version = expectedVersion;
        for (var event : events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();

            var persistedEvent = repository.save(eventModel);

            if (persistedEvent != null) {
                // TODO: produce event to kafka
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = repository.findByAggregateIdentifier(aggregateId);
        if (eventStream.isEmpty() || eventStream == null) {
            throw new AggregateNotFoundException("Incorrect account ID provided. ");
        }

        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
