package com.codeflix.admin.catalog.infrastructure.genre.presenters;

import com.codeflix.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.admin.catalog.application.genre.retrieve.get.GenreOutput;
import com.codeflix.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;

public final class GenreApiPresenter {
    private GenreApiPresenter() {}

    public static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
