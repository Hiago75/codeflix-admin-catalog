package com.codeflix.admin.catalog.application.castmember.update;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@IntegrationTest
public class UpdateCastMemberUseCaseIT {
    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway).findById(any());

        final var actualPersistedMember =
                this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualPersistedMember.getName());
        assertEquals(expectedType, actualPersistedMember.getType());
        assertEquals(aMember.getCreatedAt(), actualPersistedMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualPersistedMember.getUpdatedAt()));

        verify(castMemberGateway).update(any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, times(0)).update(any());
    }
}
