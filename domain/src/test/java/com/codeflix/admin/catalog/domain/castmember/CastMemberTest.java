package com.codeflix.admin.catalog.domain.castmember;

import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CastMemberTest {
    @Test
    public void givenAValidParams_whenCallNewMember_thenInstantiateACastMember() {
        final var expectedName = "Actor";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        assertNotNull(actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidLengthNameMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "N".repeat(300);
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "Actor";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "Actor";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("artor", expectedType);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        actualMember.update(expectedName, expectedType);

        assertNotNull(actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newMember("artor", expectedType);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newMember("artor", expectedType);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
        final var expectedName = "Actor";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newMember(expectedName, CastMemberType.ACTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidLengthNameMoreThan255_shouldReceiveNotification() {
        final var expectedName = "N".repeat(300);
        final CastMemberType expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newMember("Director", expectedType);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
