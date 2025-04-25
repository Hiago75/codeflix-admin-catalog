package com.codeflix.admin.catalog.infrastructure.configuration.usecases;

import com.codeflix.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.codeflix.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.codeflix.admin.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoUseCaseConfig {
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(final VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
