package com.codeflix.admin.catalog.application.castmember.delete;

import com.codeflix.admin.catalog.application.Fixture;
import com.codeflix.admin.catalog.application.UseCaseTest;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValid_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = aMember.getId();

        doNothing().when(castMemberGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalid_whenCallsDeleteCastMember_shouldReturnOk() {
        final var expectedId = CastMemberID.from("123");

        doNothing().when(castMemberGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValid_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = aMember.getId();

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));
    }
}
