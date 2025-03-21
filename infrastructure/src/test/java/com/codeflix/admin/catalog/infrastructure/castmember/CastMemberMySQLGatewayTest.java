package com.codeflix.admin.catalog.infrastructure.castmember;

import com.codeflix.admin.catalog.Fixture;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.MySQLGatewayTest;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {
    @Autowired
    private CastMemberMySQLGateway castMemberMySQLGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        assertEquals(0, castMemberRepository.count());

        final var actualMember = castMemberMySQLGateway.create(CastMember.with(aMember));

        assertEquals(1, castMemberRepository.count());
        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldPersistIt() {
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Random", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());
        assertEquals("Random", currentMember.getName());
        assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        final var actualMember = castMemberMySQLGateway.update(
                CastMember.with(aMember).update(expectedName, expectedType)
        );

        assertEquals(1, castMemberRepository.count());
        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallsFindById_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberMySQLGateway.findById(expectedId).get();

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberMySQLGateway.findById(CastMemberID.from("123"));

        assertTrue(actualMember.isEmpty());
    }

    @Test
    public void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        castMemberMySQLGateway.deleteById(aMember.getId());

        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        castMemberMySQLGateway.deleteById(CastMemberID.from("123"));

        assertEquals(1, castMemberRepository.count());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) {
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }

    private void mockMembers() {
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
        ));
    }
}