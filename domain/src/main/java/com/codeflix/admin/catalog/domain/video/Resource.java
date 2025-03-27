package com.codeflix.admin.catalog.domain.video;

import com.codeflix.admin.catalog.domain.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {
    private final byte[] content;
    private final String contentType;
    private final String name;
    private final VideoMediaType type;

    private Resource(
            final byte[] content,
            final String contentType,
            final String name,
            final VideoMediaType type
    ) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public static Resource of(
            final byte[] content,
            final String contentType,
            final String name,
            final VideoMediaType type
    ) {
        return new Resource(content, contentType, name, type);
    }

    public byte[] content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public String name() {
        return name;
    }

    public VideoMediaType type() {
        return type;
    }
}
