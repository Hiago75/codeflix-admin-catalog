package com.codeflix.admin.catalog.application.category.retrieve.get;

import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.Error;

import java.util.Objects;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        final var anCategoryId = CategoryID.from(anId);

        return this.categoryGateway.findById(anCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(() -> notFound(anCategoryId));
    }

    private static DomainException notFound(CategoryID anId) {
        return DomainException.with(
                new Error("Category with ID %s was not found".formatted(anId.getValue()))
        );
    }
}
