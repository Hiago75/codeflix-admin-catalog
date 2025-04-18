package com.codeflix.admin.catalog.infrastructure.castmember.models;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class UpdateCastMemberRequestTest {
    @Autowired
    private JacksonTester<UpdateCastMemberRequest> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var json = """
        {
            "name": "%s",
            "type": "%s"
        }
        """.formatted(expectedName, expectedType);

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}
