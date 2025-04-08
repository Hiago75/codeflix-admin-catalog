package com.codeflix.admin.catalog.domain.video;

import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallNewVideo_shouldInstantiateAVideo() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers =  Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers =  Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                "Test title",
                "Test desc",
                Year.of(1900),
                0.0,
                Rating.AGE_10,
                true,
                true,
                Set.of(),
                Set.of(),
                Set.of()
        );

        final var actualVideo = Video.with(aVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers =  Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aVideoMedia = AudioVideoMedia.with("id", "avc", "video.mp4", "/123/videos", "", MediaStatus.PENDING);

        final var actualVideo = Video.with(aVideo).setVideo(aVideoMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aVideoMedia, actualVideo.getVideo().get());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aTrailerMedia = AudioVideoMedia.with("id", "avc", "trailer.mp4", "/123/videos", "", MediaStatus.PENDING);

        final var actualVideo = Video.with(aVideo).setTrailer(aTrailerMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aTrailerMedia, actualVideo.getTrailer().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aBannerMedia = ImageMedia.with("avc", "trailer.mp4", "/123/videos");

        final var actualVideo = Video.with(aVideo).setBanner(aBannerMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aBannerMedia, actualVideo.getBanner().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbMedia = ImageMedia.with("avc", "trailer.mp4", "/123/videos");

        final var actualVideo = Video.with(aVideo).setThumbnail(aThumbMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aThumbMedia, actualVideo.getThumbnail().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdated() {
        final var expectedTitle = "System design";
        final var expectedDescription = "Estudo de caso";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbnailHalfMedia = ImageMedia.with("avc", "trailer.mp4", "/123/videos");

        final var actualVideo = Video.with(aVideo).setThumbnailHalf(aThumbnailHalfMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration(), 0.01);
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aThumbnailHalfMedia, actualVideo.getThumbnailHalf().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}
