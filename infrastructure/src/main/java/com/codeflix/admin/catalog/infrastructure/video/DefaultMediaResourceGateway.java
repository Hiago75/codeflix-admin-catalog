package com.codeflix.admin.catalog.infrastructure.video;

import com.codeflix.admin.catalog.domain.resource.Resource;
import com.codeflix.admin.catalog.domain.video.*;
import com.codeflix.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.codeflix.admin.catalog.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {
    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filepath = filepath(anId, videoResource.type());
        final var aResource = videoResource.resource();

        store(filepath, aResource);

        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filepath = filepath(anId, videoResource.type());
        final var aResource = videoResource.resource();

        store(filepath, aResource);

        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public Optional<Resource> getResource(final VideoID anId, final VideoMediaType type) {
        return this.storageService.get(filepath(anId, type));
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(folder(anId));

        this.storageService.deleteAll(ids);
    }

    private String filename(final VideoMediaType aType) {
        return filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filepath(final VideoID anId, final VideoMediaType type) {
        return folder(anId)
                .concat("/")
                .concat(filename(type));
    }

    private void store(final String filepath, final Resource aResource) {
        this.storageService.store(filepath, aResource);
    }
}
