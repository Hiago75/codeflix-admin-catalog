package com.codeflix.admin.catalog.application.video.retrieve.list;

import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.video.VideoGateway;
import com.codeflix.admin.catalog.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase{
    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aSearchQuery) {
        return videoGateway.findAll(aSearchQuery)
                .map(VideoListOutput::from);
    }
}
