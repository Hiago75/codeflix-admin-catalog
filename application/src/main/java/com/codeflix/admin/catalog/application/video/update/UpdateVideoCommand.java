package com.codeflix.admin.catalog.application.video.update;

import com.codeflix.admin.catalog.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

public record UpdateVideoCommand(
        String id,
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf
) {
    public static UpdateVideoCommand with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource video,
            final Resource trailer,
            final Resource banner,
            final Resource thumbnail,
            final Resource thumbnailHalf
    ) {
        return new UpdateVideoCommand(
                id,
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                members,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf
        );
    }

    public static UpdateVideoCommand with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members
    ) {
        return new UpdateVideoCommand(
                id,
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                members,
                null, // video
                null, // trailer
                null, // banner
                null, // thumbnail
                null  // thumbnailHalf
        );
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}
