package com.codeflix.admin.catalog.application.video.retrieve.get;

import com.codeflix.admin.catalog.application.UseCaseTest;
import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.video.Video;
import com.codeflix.admin.catalog.domain.video.VideoGateway;
import com.codeflix.admin.catalog.domain.video.VideoID;
import com.codeflix.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.codeflix.admin.catalog.domain.Fixture.audioVideo;
import static com.codeflix.admin.catalog.domain.Fixture.image;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldReturnIt() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.documentary().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.johnDoe().getId(),
                Fixture.CastMembers.janeDoe().getId()
        );
        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = image(VideoMediaType.BANNER);
        final var expectedThumb = image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualVideo = this.useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualVideo.id());
        assertEquals(expectedTitle, actualVideo.title());
        assertEquals(expectedDescription, actualVideo.description());
        assertEquals(expectedLaunchYear.getValue(), actualVideo.launchedAt());
        assertEquals(expectedDuration, actualVideo.duration());
        assertEquals(expectedOpened, actualVideo.opened());
        assertEquals(expectedPublished, actualVideo.published());
        assertEquals(expectedRating, actualVideo.rating());
        assertEquals(asString(expectedCategories), actualVideo.categories());
        assertEquals(asString(expectedGenres), actualVideo.genres());
        assertEquals(asString(expectedMembers), actualVideo.castMembers());
        assertEquals(expectedVideo, actualVideo.video());
        assertEquals(expectedTrailer, actualVideo.trailer());
        assertEquals(expectedBanner, actualVideo.banner());
        assertEquals(expectedThumb, actualVideo.thumbnail());
        assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
        assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        final var expectedErrorMessage = "Video with ID 123 was not found";

        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualError = assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualError.getMessage());
    }
}
