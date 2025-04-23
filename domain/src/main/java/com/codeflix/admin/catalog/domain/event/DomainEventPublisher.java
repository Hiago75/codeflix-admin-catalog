package com.codeflix.admin.catalog.domain.event;

@FunctionalInterface
public interface DomainEventPublisher {
    <T extends DomainEvent> void publishEvent(final T event);
}
