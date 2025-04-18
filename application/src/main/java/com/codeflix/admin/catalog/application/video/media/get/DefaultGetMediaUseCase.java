package com.codeflix.admin.catalog.application.video.media.get;

import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalog.domain.video.VideoID;
import com.codeflix.admin.catalog.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase{
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(GetMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aType = VideoMediaType.of(aCommand.mediaType())
                .orElseThrow(() -> typeNotFound(aCommand.mediaType()));

        final var aResource = this.mediaResourceGateway.getResource(anId, aType)
                .orElseThrow(() -> notFound(aCommand.videoId(), aCommand.mediaType()));

        return MediaOutput.with(aResource);
    }

    private DomainException notFound(final String anId, final String aType) {
        return NotFoundException.with(
                new Error("Resource %s not found for video %s".formatted(aType, anId))
        );
    }

    private DomainException typeNotFound(final String mediaType) {
        return NotFoundException.with(
                new Error("Media type %s doesn't exists".formatted(mediaType))
        );
    }
}
