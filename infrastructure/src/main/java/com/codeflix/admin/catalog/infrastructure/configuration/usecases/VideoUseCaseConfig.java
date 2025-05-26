package com.codeflix.admin.catalog.infrastructure.configuration.usecases;

import com.codeflix.admin.catalog.application.video.create.CreateVideoUseCase;
import com.codeflix.admin.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.codeflix.admin.catalog.application.video.delete.DefaultDeleteVideoUseCase;
import com.codeflix.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.codeflix.admin.catalog.application.video.media.get.DefaultGetMediaUseCase;
import com.codeflix.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.codeflix.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.codeflix.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.codeflix.admin.catalog.application.video.media.upload.DefaultUploadMediaUseCase;
import com.codeflix.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.list.DefaultListVideosUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.codeflix.admin.catalog.application.video.update.DefaultUpdateVideoUseCase;
import com.codeflix.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.codeflix.admin.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.catalog.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, castMemberGateway, genreGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(categoryGateway, castMemberGateway, genreGateway,videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
