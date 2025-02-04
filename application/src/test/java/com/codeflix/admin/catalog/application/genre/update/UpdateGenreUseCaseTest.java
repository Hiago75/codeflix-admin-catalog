package com.codeflix.admin.catalog.application.genre.update;

import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {
    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreID() {
        final var aGenre = Genre.newGenre("action", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                expectedCategories
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);

        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
            Objects.equals(expectedId, aUpdatedGenre.getId())
                && Objects.equals(expectedName, aUpdatedGenre.getName())
                && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }
}
