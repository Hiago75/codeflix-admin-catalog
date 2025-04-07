package com.codeflix.admin.catalog.infrastructure.video;

import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.video.*;
import com.codeflix.admin.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.codeflix.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {
    private final VideoRepository videoRepository;

    public DefaultVideoGateway(final VideoRepository videoRepository) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(Video aVideo) {
        return save(aVideo);
    }

    @Override
    public void deleteById(VideoID anId) {
        final var aVideoId = anId.getValue();

        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggretate);
    }

    @Override
    @Transactional
    public Video update(Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery aQuery) {
        return null;
    }

    private Video save(Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggretate();
    }
}
