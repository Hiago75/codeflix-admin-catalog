package com.codeflix.admin.catalog.infrastructure.castmember.models;

import com.codeflix.admin.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
