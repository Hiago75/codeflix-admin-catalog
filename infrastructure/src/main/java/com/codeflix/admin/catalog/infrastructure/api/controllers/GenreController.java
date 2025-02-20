package com.codeflix.admin.catalog.infrastructure.api.controllers;

import com.codeflix.admin.catalog.application.genre.create.CreateGenreCommand;
import com.codeflix.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.codeflix.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.codeflix.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.api.GenreAPI;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {
    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase
    ) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
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
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {

    }
}
