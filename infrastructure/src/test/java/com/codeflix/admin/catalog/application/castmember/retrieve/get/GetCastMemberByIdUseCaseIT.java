package com.codeflix.admin.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {
    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        final var actualOutput = useCase.execute(expectedId.getValue());

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedName, actualOutput.name());
        assertEquals(expectedType, actualOutput.type());
        assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

        verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenAInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway, times(1)).findById(expectedId);
    }
}
