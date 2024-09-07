package com.techbank.cqrscore.domain;

import com.techbank.cqrscore.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    @Getter
    protected String id;

    @Setter
    @Getter
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public List<BaseEvent> getUncommittedChanges() {
        return this.changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, MessageFormat.format("The apply method was not found in the aggregate for  {0}", event.getClass().getName()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error aplying event to aggregate", e);
        } finally {
            if (isNewEvent) {
                changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChange(event, false));
    }
}
