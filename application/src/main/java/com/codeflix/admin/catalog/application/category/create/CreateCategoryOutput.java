package com.codeflix.admin.catalog.application.category.create;

import com.codeflix.admin.catalog.domain.category.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from (final String anCategoryId) {
        return new CreateCategoryOutput(anCategoryId);
    }

    public static CreateCategoryOutput from (final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
