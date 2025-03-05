package com.codeflix.admin.catalog.application.castmember.create;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberOutput(
        String id
) {
    public static CreateCastMemberOutput from(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }
}
