package com.codeflix.admin.catalog.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCastMemberID implements Serializable {
    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name="cast_member_id", nullable = false)
    private String castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final String videoId, final String castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberID from(final String videoId, final String castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberID that = (VideoCastMemberID) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(castMemberId, that.castMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, castMemberId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCastMemberId() {
        return castMemberId;
    }

    public void setCastMemberId(String castMemberId) {
        this.castMemberId = castMemberId;
    }
}
