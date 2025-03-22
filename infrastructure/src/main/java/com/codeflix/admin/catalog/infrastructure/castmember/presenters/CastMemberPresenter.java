package com.codeflix.admin.catalog.infrastructure.castmember.presenters;

import com.codeflix.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.codeflix.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt(),
                aMember.updatedAt()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt()
        );
    }
}
