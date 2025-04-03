package com.codeflix.admin.catalog.domain.video;

import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static VideoID unique() {
        return VideoID.from(IdUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VideoID that = (VideoID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
