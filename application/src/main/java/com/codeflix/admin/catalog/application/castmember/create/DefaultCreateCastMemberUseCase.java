package com.codeflix.admin.catalog.application.castmember.create;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand anCommand) {
        final var aName = anCommand.name();
        final var aType = anCommand.type();

        final var notification = Notification.create();
        final var aMember = notification.validate(() -> CastMember.newMember(aName, aType));

        if(notification.hasErrors()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private static void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }
}
