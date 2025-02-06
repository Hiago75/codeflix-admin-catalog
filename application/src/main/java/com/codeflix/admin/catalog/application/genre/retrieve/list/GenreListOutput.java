package com.codeflix.admin.catalog.application.genre.retrieve.list;

import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {
    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream()
                        .map(CategoryID::getValue)
                        .toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
