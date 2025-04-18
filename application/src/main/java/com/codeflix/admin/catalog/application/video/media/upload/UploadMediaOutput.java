package com.codeflix.admin.catalog.application.video.media.upload;

import com.codeflix.admin.catalog.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {
    public static UploadMediaOutput with(
            final String videoId,
            final VideoMediaType aType
    ) {
        return new UploadMediaOutput(videoId, aType);
    }
}
