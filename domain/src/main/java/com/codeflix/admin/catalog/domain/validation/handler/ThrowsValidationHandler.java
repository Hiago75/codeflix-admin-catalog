package com.codeflix.admin.catalog.domain.validation.handler;

import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
       try {
           return aValidation.validate();
       } catch (final Exception ex) {
           throw DomainException.with(new Error(ex.getMessage()));
       }
    }

    @Override
    public boolean hasErrors() {
        return ValidationHandler.super.hasErrors();
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
