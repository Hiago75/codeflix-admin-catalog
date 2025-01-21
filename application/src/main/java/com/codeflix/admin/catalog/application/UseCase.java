package com.codeflix.admin.catalog.application;

import com.codeflix.admin.catalog.domain.category.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}