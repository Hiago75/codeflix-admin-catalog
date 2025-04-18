package com.codeflix.admin.catalog.domain.exceptions;

import com.codeflix.admin.catalog.domain.AggregateRoot;
import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {
    protected NotFoundException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier anId
            ) {
        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                anId.getValue()
        );

        return new NotFoundException(anError, Collections.emptyList());
    }

    public static NotFoundException with(final Error error) {
        return new NotFoundException(error.message(), List.of(error));
    }
}
