package com.codeflix.admin.catalog.infrastructure.castmember;

import com.codeflix.admin.catalog.Fixture;
import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.MySQLGatewayTest;
import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        assertEquals(aMember.getCreatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        assertEquals(aMember.getCreatedAt(), persistedMember.getUpdatedAt());
    }
}