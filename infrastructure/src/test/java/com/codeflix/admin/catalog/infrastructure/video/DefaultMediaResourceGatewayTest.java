package com.codeflix.admin.catalog.infrastructure.video;

import com.codeflix.admin.catalog.IntegrationTest;
import com.codeflix.admin.catalog.domain.video.*;
import com.codeflix.admin.catalog.infrastructure.services.StorageService;
import com.codeflix.admin.catalog.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.codeflix.admin.catalog.domain.Fixture.Videos.mediaType;
import static com.codeflix.admin.catalog.domain.Fixture.Videos.resource;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        storageService().clear();
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var actualMedia =
                this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        final var actualMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.location());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), expectedType), expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType).get();

        Assertions.assertEquals(expectedResource, actualResult);
    }

    @Test
    public void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.THUMBNAIL;

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, resource(mediaType())));
        expectedValues.forEach(id -> storageService().store(id, resource(mediaType())));

        Assertions.assertEquals(5, storageService().storage().size());

        this.mediaResourceGateway.clearResources(videoOne);

        Assertions.assertEquals(2, storageService().storage().size());

        final var actualKeys = storageService().storage().keySet();

        Assertions.assertTrue(
                expectedValues.size() == actualKeys.size()
                        && actualKeys.containsAll(expectedValues)
        );
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }
}