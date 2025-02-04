package com.codeflix.admin.catalog.domain.validation;

import java.util.List;
import com.codeflix.admin.catalog.domain.validation.Error;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    default boolean hasErrors() {
        return getErrors() != null && getErrors().isEmpty();
    }

    List<Error> getErrors();

    interface Validation<T> {
        T validate();
    }
}
