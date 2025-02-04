package com.codeflix.admin.catalog.application.genre.create;

import com.codeflix.admin.catalog.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {
    public static CreateGenreOutput from (final String anGenre) {
        return new CreateGenreOutput(anGenre);
    }

    public static CreateGenreOutput from (final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
