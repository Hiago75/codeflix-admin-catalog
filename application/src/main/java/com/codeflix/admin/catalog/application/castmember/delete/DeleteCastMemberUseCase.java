package com.codeflix.admin.catalog.application.castmember.delete;

import com.codeflix.admin.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
