package com.codeflix.admin.catalog.application.genre.delete;

import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {
    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(
            final GenreGateway genreGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }
}
