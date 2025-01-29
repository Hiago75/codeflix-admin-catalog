package com.codeflix.admin.catalog.infrastructure.category.presenters;

import com.codeflix.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryApiOutput;

public final class CategoryApiPresenter {
    private CategoryApiPresenter() {}

    public static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
