package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.ControllerTest;
import com.codeflix.admin.catalog.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;
import com.codeflix.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.codeflix.admin.catalog.domain.validation.Error;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories =  List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var anApiInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories =  List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedError = "'name' should not be null";

        final var anApiInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedError))));

        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedError)));

        verify(createGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
}
