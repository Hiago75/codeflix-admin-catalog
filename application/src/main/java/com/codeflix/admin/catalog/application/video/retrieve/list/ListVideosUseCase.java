package com.codeflix.admin.catalog.application.video.retrieve.list;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {

}