package com.codeflix.admin.catalog.domain;

import com.codeflix.admin.catalog.domain.event.DomainEvent;
import com.codeflix.admin.catalog.domain.utils.IdUtils;
import com.codeflix.admin.catalog.domain.utils.InstantUtils;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest extends UnitTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;

        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(1, anEntity.getDomainEvents().size());

        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }

    @Test
    public void givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {

        final var expectedEvents = 1;
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(new DummyEvent());

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
    }

    @Test
    public void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherAndClearTheList() {
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());

        assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}