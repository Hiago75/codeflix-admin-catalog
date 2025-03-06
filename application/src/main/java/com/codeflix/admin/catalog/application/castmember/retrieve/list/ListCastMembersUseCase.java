package com.codeflix.admin.catalog.application.castmember.retrieve.list;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMembersUseCase {
}
