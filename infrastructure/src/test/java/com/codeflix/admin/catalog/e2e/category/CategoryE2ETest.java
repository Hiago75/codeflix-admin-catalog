package com.codeflix.admin.catalog.e2e.category;

import com.codeflix.admin.catalog.APITest;
import com.codeflix.admin.catalog.E2ETest;
import com.codeflix.admin.catalog.e2e.MockDsl;
import com.codeflix.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.category.persistance.CategoryRepository;
import com.codeflix.admin.catalog.infrastructure.configuration.json.Json;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveACategory(actualId);

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());


        givenACategory("Movies",null, true);
        givenACategory("Documentaries",null, true);
        givenACategory("Shows",null, true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Shows")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        givenACategory("Movies",null, true);
        givenACategory("Documentaries",null, true);
        givenACategory("Shows",null, true);

        listCategories(0, 1, "mov", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        givenACategory("Movies","C", true);
        givenACategory("Documentaries","Z", true);
        givenACategory("Shows","A", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Movies")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Shows")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToRetrieveACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByRetrievingANotFoundCategory() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/categories/123").with(APITest.CATEGORIZATION_JWT);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var actualId = givenACategory("Movie", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/" + actualId.getValue())
                .with(APITest.CATEGORIZATION_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

       this.mvc.perform(aRequest)
                .andExpect(status().isOk());

       final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var actualId = givenACategory("Movie", null, true);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/" + actualId.getValue())
                .with(APITest.CATEGORIZATION_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory("Movie", null, false);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

       updateACategory(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        deleteACategory(actualId);

        assertFalse(this.categoryRepository.existsById(actualId.getValue()));
    }
}
