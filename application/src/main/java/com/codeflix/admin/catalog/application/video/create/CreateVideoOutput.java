package com.codeflix.admin.catalog.application.video.create;

import com.codeflix.admin.catalog.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput with(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
