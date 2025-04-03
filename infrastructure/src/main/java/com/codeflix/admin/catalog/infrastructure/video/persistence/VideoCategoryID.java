package com.codeflix.admin.catalog.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCategoryID implements Serializable {
    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name="category_id", nullable = false)
    private String categoryId;

    public VideoCategoryID() {
    }

    private VideoCategoryID(final String videoId, final String categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryID from(final String videoId, final String categoryId) {
        return new VideoCategoryID(videoId, categoryId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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
