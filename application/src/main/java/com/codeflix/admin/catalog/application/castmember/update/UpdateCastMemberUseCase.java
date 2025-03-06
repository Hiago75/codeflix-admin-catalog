package com.codeflix.admin.catalog.application.castmember.update;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.codeflix.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {

}
