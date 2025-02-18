package com.codeflix.admin.catalog.application.genre.create;

import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private DefaultCreateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var movies = Category.newCategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIds().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIds())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
