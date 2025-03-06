package com.codeflix.admin.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember castMember) {
        return new CastMemberOutput(
                castMember.getId().getValue(),
                castMember.getName(),
                castMember.getType(),
                castMember.getCreatedAt(),
                castMember.getUpdatedAt()
        );
    }
}
