package com.codeflix.admin.catalog.infrastructure.api.controllers;

import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.api.GenreAPI;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController implements GenreAPI {
    @Override
    public ResponseEntity<?> createGenre(final CreateGenreRequest input) {
        return null;
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
