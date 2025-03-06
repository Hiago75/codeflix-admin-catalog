package com.codeflix.admin.catalog.application.castmember.delete;

import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase{
    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(String anId) {
        this.castMemberGateway.deleteById(CastMemberID.from(anId));
    }
}
