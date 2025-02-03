package com.codeflix.admin.catalog.domain.genre;

import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = "N".repeat(300);
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
