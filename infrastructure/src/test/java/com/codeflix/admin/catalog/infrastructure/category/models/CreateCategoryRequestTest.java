package com.codeflix.admin.catalog.infrastructure.category.models;

import com.codeflix.admin.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class CreateCategoryRequestTest {
    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var json = """
        {
            "name": "%s",
            "description": "%s",
            "is_active": %s
        }
        """.formatted(expectedName, expectedDescription, expectedIsActive);

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
