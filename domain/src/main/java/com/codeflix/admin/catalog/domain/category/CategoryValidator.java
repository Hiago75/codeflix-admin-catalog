package com.codeflix.admin.catalog.domain.category;

import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        if (this.category.getName() == null || this.category.getName().isEmpty()) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
