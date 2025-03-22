package com.codeflix.admin.catalog.infrastructure.api.controllers;

import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.codeflix.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.codeflix.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.codeflix.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.api.CastMemberAPI;
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

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
    }


    @Override
    public ResponseEntity<?> createCastsMember(CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.castMemberType());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<Object> listCastMembers(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CastMemberResponse getById(String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand = UpdateCastMemberCommand.with(id, aBody.name(), aBody.castMemberType());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(String id) {

    }
}
