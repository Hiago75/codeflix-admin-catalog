package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.ControllerTest;
import com.codeflix.admin.catalog.application.video.create.CreateVideoCommand;
import com.codeflix.admin.catalog.application.video.create.CreateVideoOutput;
import com.codeflix.admin.catalog.application.video.create.CreateVideoUseCase;
import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.video.VideoID;
import com.codeflix.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Year;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

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
}