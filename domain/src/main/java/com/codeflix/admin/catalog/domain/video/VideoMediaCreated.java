package com.codeflix.admin.catalog.domain.video;

import com.codeflix.admin.catalog.domain.event.DomainEvent;
import com.codeflix.admin.catalog.domain.utils.InstantUtils;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {
    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}
