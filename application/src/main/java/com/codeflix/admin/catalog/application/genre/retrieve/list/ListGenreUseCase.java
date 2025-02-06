package com.codeflix.admin.catalog.application.genre.retrieve.list;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

}
