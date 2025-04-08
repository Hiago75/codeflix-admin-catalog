package com.codeflix.admin.catalog.e2e.castmember;

import com.codeflix.admin.catalog.E2ETest;
import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.e2e.MockDsl;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

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
    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var actualMemberId = givenACastMember(expectedName, expectedType);

        final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";

        givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Jason Momoa")));

        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchThruAllMembers() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1, "vin")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllMembersByNameDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Jason Momoa")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualMember = retrieveACastMember(actualId);

        assertEquals(expectedName, actualMember.name());
        assertEquals(expectedType.name(), actualMember.type().name());
        assertNotNull(actualMember.createdAt());
        assertNotNull(actualMember.updatedAt());
        assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("CastMember with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType)
                .andExpect(status().isOk());

        final var actualMember = retrieveACastMember(actualId);

        assertEquals(expectedName, actualMember.name());
        assertEquals(expectedType.name(), actualMember.type().name());
        assertNotNull(actualMember.createdAt());
        assertNotNull(actualMember.updatedAt());
        assertNotEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingACastMemberWithInvalidValue() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        assertEquals(2, castMemberRepository.count());

        deleteACastMember(actualId)
                .andExpect(status().isNoContent());

        assertEquals(1, castMemberRepository.count());
        assertFalse(castMemberRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberWithInvalidIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        assertEquals(2, castMemberRepository.count());

        deleteACastMember(CastMemberID.from("123"))
                .andExpect(status().isNoContent());

        assertEquals(2, castMemberRepository.count());
    }
}
