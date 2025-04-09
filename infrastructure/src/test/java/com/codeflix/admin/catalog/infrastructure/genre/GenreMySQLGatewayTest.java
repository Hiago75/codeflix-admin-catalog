package com.codeflix.admin.catalog.infrastructure.genre;

import com.codeflix.admin.catalog.MySQLGatewayTest;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.genre.GenreID;
import com.codeflix.admin.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.catalog.infrastructure.category.CategoryMySQLGateway;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldReturnAPersistGenre() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

       assertEquals(expectedId, actualGenre.getId());
       assertEquals(expectedName, actualGenre.getName());
       assertEquals(expectedIsActive, actualGenre.isActive());
       assertEquals(expectedCategories, actualGenre.getCategories());
       assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
       assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
       assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
       assertNull(actualGenre.getDeletedAt());

       final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldReturnAPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldReturnAPersistGenre() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));
        final var shows = categoryGateway.create(Category.newCategory("Shows", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), shows.getId());

        final var aGenre = Genre.newGenre("ation", expectedIsActive);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals("ation" , aGenre.getName());
        assertEquals(0, aGenre.getCategories().size());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sortedCategories(expectedCategories), sortedCategories(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(sortedCategories(expectedCategories), sortedCategories(persistedGenre.getCategoryIds()));
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreWithoutCategories_shouldReturnAUpdatedGenre() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));
        final var shows = categoryGateway.create(Category.newCategory("Shows", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ation", expectedIsActive);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals("ation" , aGenre.getName());
        aGenre.addCategories(List.of(movies.getId(), shows.getId()));
        assertEquals(2, aGenre.getCategories().size());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldReturnAUpdatedGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertFalse(aGenre.isActive());
        assertNotNull(aGenre.getDeletedAt());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldReturnAUpdatedGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        final var aGenre = Genre.newGenre(" Action" , true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(1, genreRepository.count());

        genreGateway.deleteById(aGenre.getId());

        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        assertEquals(0, genreRepository.count());

        genreGateway.deleteById(GenreID.from(" 123"));

        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCalledFindByID_shouldReturnGenre() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));
        final var shows = categoryGateway.create(Category.newCategory("Shows", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), shows.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(1, genreRepository.count());

        final var actualGenre = genreGateway.findById(aGenre.getId()).get();

        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sortedCategories(expectedCategories), sortedCategories(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(actualGenre.getUpdatedAt(), aGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenANonPersistedGenre_whenCalledFindByID_shouldReturnGenre() {
       final var expectedId = GenreID.from("123");

       assertEquals(0, genreRepository.count());

       final var actualGenre = genreGateway.findById(expectedId);

       assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenTwoGenresAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
        final var aGenre = Genre.newGenre("Genre 1", true);

        final var expectedItems = 1;
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var actualGenre = genreGateway.existsByIds(List.of(GenreID.from("123"), expectedId));

        assertEquals(expectedItems, actualGenre.size());
        assertEquals(expectedId.getValue(), actualGenre.get(0).getValue());
    }


    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
        "ac,0,10,1,1,Action",
        "dr,0,10,1,1,Drama",
        "com,0,10,1,1,Comedy",
        "fic,0,10,1,1,Science Fiction",
        "hor,0,10,1,1,Horror",
    })
    public void givenAValidTerm_whenCallFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Action",
            "name,desc,0,10,5,5,Science Fiction",
            "createdAt,asc,0,10,5,5,Comedy",
            "createdAt,desc,0,10,5,5,Science Fiction",
    })
    public void givenAValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Action;Comedy",
            "1,2,2,5,Drama;Horror",
            "2,2,1,5,Science Fiction",
    })
    public void givenAValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedGenres.split(";")) {
            assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }


    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comedy", true)),
                GenreJpaEntity.from(Genre.newGenre("Action", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Horror", true)),
                GenreJpaEntity.from(Genre.newGenre("Science Fiction", true))
        ));
    }

    private List<CategoryID> sortedCategories(final List<CategoryID> expectedCategories) {
        return expectedCategories.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }
}
