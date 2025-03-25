package com.codeflix.admin.catalog.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageMediaTest {
    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/abc";

        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        assertNotNull(actualImage);
        assertEquals(expectedChecksum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/abc";

        final var image1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);
        final var image2 = ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        assertEquals(image1, image2);
        assertNotSame(image1, image2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class, () ->
                ImageMedia.with(null, "Banner.png", "/images/abc")
        );

        assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", null, "/images/abc")
        );

        assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", "Banner.png", null)
        );
    }
}