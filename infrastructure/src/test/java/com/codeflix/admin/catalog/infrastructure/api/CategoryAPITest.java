package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.APITest;
import com.codeflix.admin.catalog.ControllerTest;
import com.codeflix.admin.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.codeflix.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;
import com.codeflix.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var anApiInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(APITest.CATEGORIZATION_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedError = "'name' should not be null";

        final var anApiInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedError))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(APITest.CATEGORIZATION_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedError)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedError = "'name' should not be null";

        final var anApiInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedError)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(APITest.CATEGORIZATION_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedError)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedError)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        final var request = get("/categories/{id}", expectedId.getValue())
                .with(APITest.CATEGORIZATION_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() throws Exception  {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = get("/categories/{id}", expectedId.getValue())
                .with(APITest.CATEGORIZATION_JWT);


        final var response = this.mvc.perform(request);

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() throws Exception {
        final var expectedId  = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/{id}", expectedId)
                .with(APITest.CATEGORIZATION_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        final var expectedId  = "not-found";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/{id}", expectedId)
                .with(APITest.CATEGORIZATION_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(any());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        final var expectedId  = "not-found";
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedError = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedError))));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/{id}", expectedId)
                .with(APITest.CATEGORIZATION_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedError)));

        verify(updateCategoryUseCase, times(1)).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase).execute(any());

        final var request = delete("/categories/{id}", expectedId)
            .with(APITest.CATEGORIZATION_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/categories")
                .with(APITest.CATEGORIZATION_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
            Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
