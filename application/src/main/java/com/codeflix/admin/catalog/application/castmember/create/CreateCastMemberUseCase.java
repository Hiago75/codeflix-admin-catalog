package com.codeflix.admin.catalog.application.castmember.create;

import com.codeflix.admin.catalog.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {

}
