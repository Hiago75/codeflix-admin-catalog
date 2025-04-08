package com.codeflix.admin.catalog.infrastructure.video.persistence;

import com.codeflix.admin.catalog.domain.video.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {
    @Override
    public String convertToDatabaseColumn(final Rating rating) {
        if (rating == null) {
            return null;
        }

        return rating.getName();
    }

    @Override
    public Rating convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }

        return Rating.of(dbData).orElse(null);
    }
}
