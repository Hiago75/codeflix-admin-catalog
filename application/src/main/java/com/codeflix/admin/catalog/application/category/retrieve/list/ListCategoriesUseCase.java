package com.codeflix.admin.catalog.application.category.retrieve.list;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
