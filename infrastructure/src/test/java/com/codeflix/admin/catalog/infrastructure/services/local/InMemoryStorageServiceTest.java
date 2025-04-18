package com.codeflix.admin.catalog.infrastructure.services.local;

import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.utils.IdUtils;
import com.codeflix.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageServiceTest {
    private final InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        this.target.clear();
    }

    @Test
    public void givenValidResource_whenCallsStore_thenShouldStoreIt() {
        final var expectedName = "test.txt";
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        this.target.store(expectedName, expectedResource);

        assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_thenShouldRetrieveIt() {
        final var expectedName = "test.txt";
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        target.storage().put(expectedName, expectedResource);

        final var actualResource = this.target.get(expectedName).get();

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_thenShouldBeEmpty() {
        final var expectedName = "test.txt";

        final var actualResource = this.target.get(expectedName);

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_thenShouldRetrieveAll() {
        final var expectedNames = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );

        final var allNames = new ArrayList<>(expectedNames);
        allNames.add("image_" + IdUtils.uuid());
        allNames.add("image_" + IdUtils.uuid());

        allNames.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, target.storage().size());

        final var actualResource = this.target.list("video");

        assertTrue(
            expectedNames.size() == actualResource.size()
            && expectedNames.containsAll(actualResource)
        );
    }

    @Test
    public void givenValidNames_whenCallsDelete_thenShouldDeleteAll() {
        final var expectedNames = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );

        final var expectedResult = Set.of(
                "image_" + IdUtils.uuid(),
                "image_" + IdUtils.uuid()
        );

        final var allNames = new ArrayList<>(expectedNames);
        allNames.addAll(expectedResult);

        allNames.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, target.storage().size());

        this.target.deleteAll(expectedNames);


        assertEquals(2, target.storage().size());
        assertTrue(
                expectedResult.size() == target.storage().size()
                        && expectedResult.containsAll(target.storage().keySet())
        );
    }
}