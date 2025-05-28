package com.codeflix.admin.catalog.application.castmember.delete;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValid_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var aMember2 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember2));
        assertEquals(2, this.castMemberRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));

        assertEquals(1, this.castMemberRepository.count());
        assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        assertTrue(this.castMemberRepository.existsById(aMember2.getId().getValue()));
    }

    @Test
    public void givenAnInvalid_whenCallsDeleteCastMember_shouldReturnOk() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = CastMemberID.from("123");

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));
        assertEquals(1, this.castMemberRepository.count());
        assertTrue(this.castMemberRepository.existsById(aMember.getId().getValue()));
    }

    @Test
    public void givenAValid_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq(expectedId));

        assertEquals(1, this.castMemberRepository.count());

    }
}
