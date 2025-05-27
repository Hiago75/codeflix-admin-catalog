package com.codeflix.admin.catalog.e2e.genre;

import com.codeflix.admin.catalog.APITest;
import com.codeflix.admin.catalog.E2ETest;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.e2e.MockDsl;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {
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

    @Override
    public MockMvc mvc() {
        return this.mvc;
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

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", List.of(), true);
        givenAGenre("Sports",List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", List.of(), true);
        givenAGenre("Sports",List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 1, "spor", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenresByDescriptionDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", List.of(), true);
        givenAGenre("Sports",List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToRetrieveAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);
        final var expectedName = "Action";
        final var expectedCategories = List.of(movies);
        final var expectedIsActive = true;

        final var actualId = givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = retrieveAGenre(actualId);

        assertEquals(expectedName, actualGenre.name());
        assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        assertEquals(expectedIsActive, actualGenre.active());
        assertNotNull(actualGenre.createdAt());
        assertNotNull(actualGenre.updatedAt());
        assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByRetrievingANotFoundGenre() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/genres/123").with(APITest.CATEGORIZATION_JWT);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedCategories = List.of(movies);
        final var expectedIsActive = true;

        final var actualId = givenAGenre("ation", expectedCategories, expectedIsActive);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isOk());

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
    public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedCategories = List.of(movies);
        final var expectedIsActive = false;

        final var actualId = givenAGenre(expectedName, expectedCategories, true);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isOk());

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIds())
        );
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedCategories = List.<CategoryID>of();
        final var expectedIsActive = true;

        final var actualId = givenAGenre(expectedName, expectedCategories, false);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isOk());

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
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);
        final var actualId = givenAGenre( "Action", List.of(movies), true);

        deleteAGenre(actualId).andExpect(status().isNoContent());

        assertFalse(this.genreRepository.existsById(actualId.getValue()));
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void asACatalogAdminIShouldNotReceiveAnErrorWhenDeletingANonExistingGenre() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        deleteAGenre(GenreID.from("123")).andExpect(status().isNoContent());

        assertEquals(0, genreRepository.count());
    }
}
