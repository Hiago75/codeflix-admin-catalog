package com.codeflix.admin.catalog.application.castmember.retrieve.list;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMemberUseCaseIT {
    @Autowired
    private ListCastMembersUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        this.castMemberRepository.saveAllAndFlush(members.stream().map(CastMemberJpaEntity::from).toList());

        assertEquals(2, this.castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertTrue(
                expectedItems.size() == actualOutput.items().size()
                && expectedItems.containsAll(actualOutput.items())
        );

        verify(castMemberGateway).findAll(any());
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        assertEquals(0, this.castMemberRepository.count());

        final var expectedItems = List.<CastMemberListOutput>of();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(any());
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(castMemberGateway).findAll(any());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualException = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aQuery);
        });

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}
