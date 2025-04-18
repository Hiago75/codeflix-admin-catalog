package com.codeflix.admin.catalog.application.video.media.get;

import com.codeflix.admin.catalog.application.UseCaseTest;
import com.codeflix.admin.catalog.domain.Fixture;
import com.codeflix.admin.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalog.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualResult = this.useCase.execute(aCmd);


        assertEquals(expectedResource.name(), actualResult.name());
        assertEquals(expectedResource.content(), actualResult.content());
        assertEquals(expectedResource.contentType(), actualResult.contentType());
    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media type QUALQUER doesn't exists";

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), "QUALQUER");

        final var actualException = assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
