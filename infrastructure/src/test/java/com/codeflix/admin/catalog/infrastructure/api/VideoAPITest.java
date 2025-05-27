package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.APITest;
import com.codeflix.admin.catalog.ControllerTest;
import com.codeflix.admin.catalog.application.video.create.CreateVideoCommand;
import com.codeflix.admin.catalog.application.video.create.CreateVideoOutput;
import com.codeflix.admin.catalog.application.video.create.CreateVideoUseCase;
import com.codeflix.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.codeflix.admin.catalog.application.video.media.get.GetMediaCommand;
import com.codeflix.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.codeflix.admin.catalog.application.video.media.get.MediaOutput;
import com.codeflix.admin.catalog.application.video.media.upload.UploadMediaCommand;
import com.codeflix.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.codeflix.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.codeflix.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.codeflix.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.codeflix.admin.catalog.application.video.update.UpdateVideoCommand;
import com.codeflix.admin.catalog.application.video.update.UpdateVideoOutput;
import com.codeflix.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.video.*;
import com.codeflix.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.codeflix.admin.catalog.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeflix.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static com.google.common.net.HttpHeaders.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.wildfly.common.Assert.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateVideoUseCase createVideoUseCase;

    @MockitoBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockitoBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockitoBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockitoBean
    private ListVideosUseCase listVideosUseCase;

    @MockitoBean
    private GetMediaUseCase getMediaUseCase;

    @MockitoBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        final var janeDoe = Fixture.CastMembers.janeDoe();
        final var documentary = Fixture.Categories.documentary();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentary.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(janeDoe.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());

        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());

        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());

        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());

        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .with(APITest.VIDEOS_JWT)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", janeDoe.getId().getValue())
                .param("categories_id", documentary.getId().getValue())
                .param("genres_id", tech.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        assertEquals(expectedThumb.getOriginalFilename(), actualCmd.getThumbnail().get().name());
        assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.getThumbnailHalf().get().name());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = multipart("/videos")
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
        final var wesley = Fixture.CastMembers.janeDoe();
        final var documentary = Fixture.Categories.documentary();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentary.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var aCmd = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = post("/videos")
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnEmptyBody_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var aRequest = post("/videos")
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = post("/videos")
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Olá Mundo!"
                        }
                        """);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var janeDoe = Fixture.CastMembers.janeDoe();
        final var documentary = Fixture.Categories.documentary();
        final var tech = Fixture.Genres.tech();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentary.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(janeDoe.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));

        final var aRequest = get("/videos/{id}", expectedId)
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(getVideoByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        final var aRequest = get("/videos/{id}", expectedId)
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        final var janeDoe = Fixture.CastMembers.janeDoe();
        final var documentary = Fixture.Categories.documentary();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentary.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(janeDoe.getId().getValue());

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        verify(updateVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        final var janeDoe = Fixture.CastMembers.janeDoe();
        final var documentary = Fixture.Categories.documentary();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentary.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(janeDoe.getId().getValue());

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        final var aRequest = delete("/videos/{id}", expectedId.getValue()).with(APITest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isNoContent());

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        final var aVideo = new VideoPreview(Fixture.video());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .with(APITest.VIDEOS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualQuery.castMembers());
        assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
    }

    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        final var aVideo = new VideoPreview(Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertTrue(actualQuery.categories().isEmpty());
        assertTrue(actualQuery.castMembers().isEmpty());
        assertTrue(actualQuery.genres().isEmpty());
    }

    @Test
    public void givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
        final var expectedId = VideoID.unique();

        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);

        final var expectedMedia = new MediaOutput(expectedResource.content(), expectedResource.contentType(), expectedResource.name());

        when(getMediaUseCase.execute(any())).thenReturn(expectedMedia);

        final var aRequest = get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name()).with(APITest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedMedia.name())))
                .andExpect(content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(GetMediaCommand.class);

        verify(this.getMediaUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();
        assertEquals(expectedId.getValue(), actualCmd.videoId());
        assertEquals(expectedMediaType.name(), actualCmd.mediaType());
    }

    @Test
    public void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(uploadMediaUseCase.execute(any()))
                .thenReturn(new UploadMediaOutput(expectedId.getValue(), expectedType));

        final var aRequest = multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                .file(expectedVideo)
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);

        verify(this.uploadMediaUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();
        assertEquals(expectedId.getValue(), actualCmd.videoId());
        assertEquals(expectedResource.content(), actualCmd.videoResource().resource().content());
        assertEquals(expectedResource.name(), actualCmd.videoResource().resource().name());
        assertEquals(expectedResource.contentType(), actualCmd.videoResource().resource().contentType());
        assertEquals(expectedType, actualCmd.videoResource().type());
    }

    @Test
    public void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        final var aRequest = multipart("/videos/{id}/medias/INVALID", expectedId.getValue())
                .file(expectedVideo)
                .with(APITest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")));
    }
}