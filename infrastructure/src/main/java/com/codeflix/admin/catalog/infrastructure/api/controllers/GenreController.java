package com.codeflix.admin.catalog.infrastructure.api.controllers;

import com.codeflix.admin.catalog.application.genre.create.CreateGenreCommand;
import com.codeflix.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.api.GenreAPI;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {
    private final CreateGenreUseCase createGenreUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
    }

    @Override
    public ResponseEntity<?> createGenre(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.active(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> listGenre(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }
}
