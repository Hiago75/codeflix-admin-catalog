package com.codeflix.admin.catalog.e2e;

import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalog.infrastructure.genre.models.GenreResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories", anId);
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


    default  <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
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
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", directions);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final  Class<T> clazz) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("%s/".formatted(url) + anId.getValue())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anid) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete("%s/".formatted(url) + anid.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object body) throws Exception {
        return  this.mvc().perform(
                MockMvcRequestBuilders.put("%s/".formatted(url) + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body))
        );
    }
}
