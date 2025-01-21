package com.codeflix.admin.catalog.domain.validation;

import java.util.List;
import com.codeflix.admin.catalog.domain.validation.Error;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    ValidationHandler validate(Validation aValidation);

    default boolean hasErrors() {
        return getErrors() != null && getErrors().isEmpty();
    }

    List<Error> getErrors();

    public interface Validation {
        void validate();
    }
}
