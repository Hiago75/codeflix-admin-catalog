package com.codeflix.admin.catalog.infrastructure.api;

import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "genres")
@Tag(name = "Genre")
public interface GenreAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    ResponseEntity<?> createGenre(@RequestBody @Valid CreateGenreRequest input);

    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    Pagination<GenreListResponse> listGenre(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}")
    @Operation(summary = "Get a specific genre by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    GenreResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a specific genre by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre update successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody @Valid UpdateGenreRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a specific genre by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre delete successfully"),
            @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    void deleteById(@PathVariable(name = "id") String id);
}
