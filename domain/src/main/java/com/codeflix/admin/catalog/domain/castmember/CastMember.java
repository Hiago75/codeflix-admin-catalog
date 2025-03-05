package com.codeflix.admin.catalog.domain.castmember;

import com.codeflix.admin.catalog.domain.AggregateRoot;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.utils.InstantUtils;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        super(anId);

        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;

        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var anId = CastMemberID.unique();
        final var now = InstantUtils.now();

        return  new CastMember(anId, aName, aType, now, now);
    }

    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        return new CastMember(anId, aName, aType, aCreationDate, aUpdateDate);
    }

    public static CastMember with(final CastMember aMember) {
        return new CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
        );
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();

        selfValidate();

        return this;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(aHandler, this).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate(){
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }
}
