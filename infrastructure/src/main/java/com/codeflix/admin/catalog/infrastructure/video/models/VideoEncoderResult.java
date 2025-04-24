package com.codeflix.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "status")
@VideoResponseTypes
public sealed interface VideoEncoderResult
        permits VideoEncoderCompleted, VideoEncoderError{
    String getStatus();
}
