package com.codeflix.admin.catalog.domain.castmember;

import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.catalog.domain.validation.Validator;

import java.util.Objects;

public class CastMemberValidator extends Validator {
    private final CastMember castMember;
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;

    public CastMemberValidator(final ValidationHandler aHandler, final CastMember castMember) {
        super(aHandler);
        this.castMember = Objects.requireNonNull(castMember);
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();

        if (name == null) {
            this.validationHandler().append(new com.codeflix.admin.catalog.domain.validation.Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new com.codeflix.admin.catalog.domain.validation.Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }

    private void checkTypeConstraints() {
        if (this.castMember.getType() == null) {
            this.validationHandler().append(new com.codeflix.admin.catalog.domain.validation.Error("'type' should not be null"));
        }
    }
}
