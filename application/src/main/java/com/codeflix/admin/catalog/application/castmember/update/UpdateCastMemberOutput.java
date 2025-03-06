package com.codeflix.admin.catalog.application.castmember.update;

import com.codeflix.admin.catalog.domain.castmember.CastMember;

public record UpdateCastMemberOutput(
        String id
) {
    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return new UpdateCastMemberOutput(aMember.getId().getValue());
    }
}
