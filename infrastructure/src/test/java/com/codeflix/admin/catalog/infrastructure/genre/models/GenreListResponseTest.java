package com.codeflix.admin.catalog.infrastructure.genre.models;

import com.codeflix.admin.catalog.JacksonTest;
import com.codeflix.admin.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class GenreListResponseTest {

    @Autowired
    private JacksonTester<GenreListResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = "123";
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var response = new GenreListResponse(
                expectedId,
                expectedName,
                expectedIsActive,
                expectedCreatedAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt);
    }
}
