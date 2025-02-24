package com.codeflix.admin.catalog.e2e.genre;

import com.codeflix.admin.catalog.E2ETest;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("catalog_adm");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedCategories = List.<CategoryID>of();
        final var expectedIsActive = true;

        final var actualId = givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIds())
        );
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);
        final var expectedName = "Action";
        final var expectedCategories = List.of(movies);
        final var expectedIsActive = true;

        final var actualId = givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIds())
        );
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    private GenreID givenAGenre(final String aName, final List<CategoryID> categories, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);

        final var aRequest = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/genres/", "");

        return GenreID.from(actualId);
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }
}
