package com.codeflix.admin.catalog.e2e;

import com.codeflix.admin.catalog.APITest;
import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();

    default void deleteACategory(final Identifier anId) throws Exception {
        this.delete("/categories", anId);
    }

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);

        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String directions
    ) throws Exception {
        return this.list("/categories", page, perPage, search, sort, directions);
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories", anId, CategoryResponse.class);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest aRequest) throws Exception {
        return this.update("/categories", anId, aRequest);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres", anId);
    }

    default GenreID givenAGenre(final String aName, final List<CategoryID> categories, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);

        return GenreID.from(actualId);
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String directions
    ) throws Exception {
        return this.list("/genres", page, perPage, search, sort, directions);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres", anId, GenreResponse.class);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres", anId, aRequest);
    }

    default  <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    default ResultActions deleteACastMember(final CastMemberID anId) throws Exception {
        return this.delete("/cast_members/", anId);
    }

    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        final var actualId = this.given("/cast_members", aRequestBody);
        return CastMemberID.from(actualId);
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return this.givenResult("/cast_members", aRequestBody);
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default CastMemberResponse retrieveACastMember(final CastMemberID anId) throws Exception {
        return this.retrieve("/cast_members/", anId, CastMemberResponse.class);
    }

    default ResultActions retrieveACastMemberResult(final CastMemberID anId) throws Exception {
        return this.retrieveResult("/cast_members/", anId);
    }

    default ResultActions updateACastMember(final CastMemberID anId, final String aName, final CastMemberType aType) throws Exception {
        return this.update("/cast_members/", anId, new UpdateCastMemberRequest(aName, aType));
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(APITest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest);
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(APITest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String directions
    ) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("%s".formatted(url))
                .with(APITest.ADMIN_JWT)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", directions);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final  Class<T> clazz) throws Exception {
        final var aRequest = get("%s/".formatted(url) + anId.getValue())
                .with(APITest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(APITest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        return this.mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier anid) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete("%s/".formatted(url) + anid.getValue())
                .with(APITest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object body) throws Exception {
        return  this.mvc().perform(
                MockMvcRequestBuilders.put("%s/".formatted(url) + anId.getValue())
                .with(APITest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body))
        );
    }
}
