package com.codeflix.admin.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.catalog.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
