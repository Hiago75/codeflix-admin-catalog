package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "cast_members")
@Tag(name = "CastMembers")
public interface CastMemberAPI {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new cast member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    ResponseEntity<?> createCastsMember(@RequestBody @Valid CreateCastMemberRequest input);

    @GetMapping
    @Operation(summary = "List all cast members paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    Pagination<Object> listCastMembers(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}")
    @Operation(summary = "Get a specific cast member by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cast member was not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    CastMemberResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a specific cast member by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member update successfully"),
            @ApiResponse(responseCode = "404", description = "Cast member not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody @Valid UpdateCastMemberRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a specific cast member by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cast member delete successfully"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    void deleteById(@PathVariable(name = "id") String id);
}
