package com.codeflix.admin.catalog.domain;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(null, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).Message());
    }
}
