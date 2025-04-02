package com.codeflix.admin.catalog.infrastructure.video.persistence;

import com.codeflix.admin.catalog.domain.video.Rating;
import com.codeflix.admin.catalog.domain.video.Video;
import com.codeflix.admin.catalog.domain.video.VideoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {
    @Id
    private UUID id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", length = 4000)
    private String description;

    @Column(name="year_launched", nullable = false)
    private int yearLaunched;

    @Column(name="opened", nullable = false)
    private boolean opened;

    @Column(name="published", nullable = false)
    private boolean published;

    @Column(name="rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name="duration", precision=2)
    private Double duration;

    @Column(name="created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name="updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private AudioVideoMediaJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private AudioVideoMediaJpaEntity trailer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    private ImageMediaJpaEntity banner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private ImageMediaJpaEntity thumbnail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_id")
    private ImageMediaJpaEntity thumbnailHalf;

    public VideoJpaEntity() {}

    public VideoJpaEntity(
            final UUID id,
            final String title,
            final String description,
            final int yearLaunched,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final Double duration,
            final Instant createdAt,
            final Instant updatedAt, AudioVideoMediaJpaEntity video, AudioVideoMediaJpaEntity trailer, ImageMediaJpaEntity banner, ImageMediaJpaEntity thumbnail, ImageMediaJpaEntity thumbnailHalf
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
    }

    public static VideoJpaEntity from(final Video aVideo) {
        return new VideoJpaEntity(
                UUID.fromString(aVideo.getId().getValue()),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo().map(AudioVideoMediaJpaEntity::from).orElse(null),
                aVideo.getTrailer().map(AudioVideoMediaJpaEntity::from).orElse(null),
                aVideo.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
                aVideo.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
                aVideo.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null)
        );
    }

    public Video toAggretate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYearLaunched()),
                getDuration(),
                getRating(),
                isOpened(),
                isPublished(),
                getCreatedAt(),
                getUpdatedAt(),
                Optional.ofNullable(getBanner()).map(ImageMediaJpaEntity::toAggregate).orElse(null),
                Optional.ofNullable(getThumbnail()).map(ImageMediaJpaEntity::toAggregate).orElse(null),
                Optional.ofNullable(getThumbnailHalf()).map(ImageMediaJpaEntity::toAggregate).orElse(null),
                Optional.ofNullable(getTrailer()).map(AudioVideoMediaJpaEntity::toAggregate).orElse(null),
                Optional.ofNullable(getVideo()).map(AudioVideoMediaJpaEntity::toAggregate).orElse(null),
                null,
                null,
                null
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public void setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioVideoMediaJpaEntity getVideo() {
        return video;
    }

    public void setVideo(AudioVideoMediaJpaEntity video) {
        this.video = video;
    }

    public AudioVideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(AudioVideoMediaJpaEntity trailer) {
        this.trailer = trailer;
    }

    public ImageMediaJpaEntity getBanner() {
        return banner;
    }

    public void setBanner(ImageMediaJpaEntity banner) {
        this.banner = banner;
    }

    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }
}
