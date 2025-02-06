package com.codeflix.admin.catalog.application.genre.delete;


import com.codeflix.admin.catalog.application.UseCaseTest;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    public GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();

        doNothing().when(genreGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from("123");

        doNothing().when(genreGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnValiddGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }
}
