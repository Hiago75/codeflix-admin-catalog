package com.codeflix.admin.catalog.application.video.update;

import com.codeflix.admin.catalog.domain.video.Video;

public record UpdateVideoOutput(String id) {
    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
