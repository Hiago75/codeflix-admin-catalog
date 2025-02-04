package com.codeflix.admin.catalog.domain.genre;

import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = "N".repeat(300);
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOk() {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOk() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        actualGenre.activate();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidInactiveGenre_whenCallUpdateWithActive_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("action", false);

        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidActiveGenre_whenCallUpdateWithInactive_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("action", true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithInvalidEmptyName_shouldReceiveAError() {
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("action", true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualException = assertThrows(NotificationException.class, () ->
                actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithInvalidNullName_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("action", true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualException = assertThrows(NotificationException.class, () ->
                actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOk() throws InterruptedException {
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOk() throws InterruptedException {
        CategoryID seriesID = CategoryID.from("123");
        CategoryID moviesID = CategoryID.from("456");

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        Thread.sleep(1);

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallRemoveCategory_shouldReceiveOk() throws InterruptedException {
        CategoryID seriesID = CategoryID.from("123");
        CategoryID moviesID = CategoryID.from("456");

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        actualGenre.removeCategory(seriesID);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOk() throws InterruptedException {
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre("action", expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategory(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertFalse(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOk() throws InterruptedException {
        CategoryID seriesID = CategoryID.from("123");
        CategoryID moviesID = CategoryID.from("456");

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);

        actualGenre.removeCategory(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertFalse(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOk() throws InterruptedException {
        CategoryID seriesID = CategoryID.from("123");
        CategoryID moviesID = CategoryID.from("456");

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        Thread.sleep(1);

        actualGenre.addCategories(List.of(seriesID, moviesID));

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOk() throws InterruptedException {
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        Thread.sleep(1);

        actualGenre.addCategories(expectedCategories);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOk() throws InterruptedException {
        final var expectedName = "action";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        Thread.sleep(1);

        actualGenre.addCategories(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }
}
