package com.codeflix.admin.catalog.infrastructure.services.impl;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.resource.Resource;
import com.codeflix.admin.catalog.domain.utils.IdUtils;
import com.codeflix.admin.catalog.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GCStorageServiceTest {
    private GCStorageService target;
    private Storage storage;
    private final String bucket = "codeflix-test";

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldPersistIt() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        this.target.store(expectedName, expectedResource);

        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedName, actualBlob.getBlobId().getName());
        assertEquals(expectedName, actualBlob.getName());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName).get();

        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(null).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName);

        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldReturnAll() {
        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);
        final var blobVideo = mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner = mockBlob(expectedNameBanner, expectedBanner);

        final var page = Mockito.mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();
        doReturn(page).when(storage).list(anyString(), any());

        final var actualResources = this.target.list(expectedPrefix);

        verify(storage, times(1)).list(eq(this.bucket), eq(Storage.BlobListOption.prefix(expectedPrefix)));

        assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources)
        );
    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);

        this.target.deleteAll(expectedResources);

        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualResources = ((List<BlobId>) captor.getValue()).stream()
                        .map(BlobId::getName)
                        .toList();

        assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources)
        );
    }


    private Blob mockBlob(final String name, final Resource expectedResource) {
        final var blob = Mockito.mock(Blob.class);

        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(expectedResource.checksum());
        when(blob.getContent()).thenReturn(expectedResource.content());
        when(blob.getContentType()).thenReturn(expectedResource.contentType());
        when(blob.getName()).thenReturn(expectedResource.name());

        return blob;
    }
}