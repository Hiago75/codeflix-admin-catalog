package com.codeflix.admin.catalog.infrastructure.video.persistence;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.video.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCategoryID implements Serializable {
    @Column(name = "video_id", nullable = false)
    private UUID videoId;

    @Column(name="category_id", nullable = false)
    private UUID categoryId;

    public VideoCategoryID() {
    }

    private VideoCategoryID(final UUID videoId, final UUID categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryID from(final UUID videoId, final UUID categoryId) {
        return new VideoCategoryID(videoId, categoryId);
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
        this.videoId = videoId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryID that = (VideoCategoryID) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, categoryId);
    }
}
