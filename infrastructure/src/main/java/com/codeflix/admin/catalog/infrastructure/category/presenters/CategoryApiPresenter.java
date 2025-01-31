package com.codeflix.admin.catalog.infrastructure.category.presenters;

import com.codeflix.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryListResponse;

public final class CategoryApiPresenter {
    private CategoryApiPresenter() {}

    public static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
