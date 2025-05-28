package com.codeflix.admin.catalog.infrastructure.services.local;

import com.codeflix.admin.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalog.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(final Object event) {
        LOG.info("Event sent: {}", Json.writeValueAsString(event));
    }
}
