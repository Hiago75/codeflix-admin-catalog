package com.codeflix.admin.catalog.application.genre.delete;


import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DeleteGenreUseCaseIT {
    @Autowired
    private DefaultDeleteGenreUseCase useCase;

    @Autowired
    public GenreGateway genreGateway;

    @Autowired
    public GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final var expectedId = aGenre.getId();

        assertEquals(1, genreRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(1, genreRepository.count());
    }
}
