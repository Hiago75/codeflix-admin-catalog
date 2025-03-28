package com.codeflix.admin.catalog.infrastructure.castmember.models;

import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        CastMemberType type,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}
