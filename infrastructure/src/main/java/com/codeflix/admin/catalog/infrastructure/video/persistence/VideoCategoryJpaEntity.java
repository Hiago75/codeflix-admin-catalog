package com.codeflix.admin.catalog.infrastructure.video.persistence;

import com.codeflix.admin.catalog.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "videos_categories")
@Entity(name = "VideoCategory")
public class VideoCategoryJpaEntity {
    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(final VideoCategoryID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, final CategoryID categoryId) {
        return new VideoCategoryJpaEntity(
                VideoCategoryID.from(video.getId(), categoryId.getValue()),
                video
        );
    }

    public VideoCategoryID getId() {
        return id;
    }

    public void setId(VideoCategoryID id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJpaEntity video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
