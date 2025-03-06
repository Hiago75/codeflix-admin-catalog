package com.codeflix.admin.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;

public final class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase{
    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(String anId) {
        final var aMemberId = CastMemberID.from(anId);

        return this.castMemberGateway.findById(aMemberId)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId));
    }
}
