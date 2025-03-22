package com.codeflix.admin.catalog.infrastructure.api.controllers;

import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.codeflix.admin.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codeflix.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.codeflix.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.catalog.infrastructure.api.CastMemberAPI;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codeflix.admin.catalog.infrastructure.castmember.presenters.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }


    @Override
    public ResponseEntity<?> createCastsMember(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.castMemberType());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> listCastMembers(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand = UpdateCastMemberCommand.with(id, aBody.name(), aBody.castMemberType());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
