package com.codeflix.admin.catalog.infrastructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "videos")
@Tag(name = "video")
public interface VideoAPI {
}
