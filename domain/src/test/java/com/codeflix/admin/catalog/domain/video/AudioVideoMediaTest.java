package com.codeflix.admin.catalog.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioVideoMediaTest {
    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/abc";
        final var expectedEncodedLocation = "/images/abc-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualVideo = AudioVideoMedia.with(
                expectedChecksum,
                expectedName,
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        assertNotNull(actualVideo);
        assertEquals(expectedChecksum, actualVideo.checksum());
        assertEquals(expectedName, actualVideo.name());
        assertEquals(expectedRawLocation, actualVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
        assertEquals(expectedStatus, actualVideo.status());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/images/abc";
        final var expectedEncodedLocation = "/images/abc-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var video1 = AudioVideoMedia.with(
                expectedChecksum,
                "Random",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );
        final var video2 = AudioVideoMedia.with(
                expectedChecksum,
                "Simple",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        assertEquals(video1, video2);
        assertNotSame(video1, video2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/abc";
        final var expectedEncodedLocation = "/images/abc-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        null,
                        expectedName,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        expectedChecksum,
                        null,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        expectedChecksum,
                        expectedName,
                        null,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );
    }
}