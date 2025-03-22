package com.codeflix.admin.catalog.infrastructure.castmember.models;

import com.codeflix.admin.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType castMemberType) {
}
