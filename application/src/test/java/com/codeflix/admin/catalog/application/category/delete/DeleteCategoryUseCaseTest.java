package com.codeflix.admin.catalog.application.category.delete;

import com.codeflix.admin.catalog.application.UseCaseTest;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        final var expectedID = aCategory.getId();

        assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedID);
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedID = CategoryID.from("123");

        assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedID));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        final var expectedID = aCategory.getId();
        final var expectedMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedMessage))
                .when(categoryGateway).deleteById(eq(expectedID));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedID.getValue()));

        assertEquals(expectedMessage, actualException.getMessage());
        verify(categoryGateway, times(1)).deleteById(expectedID);
    }
}
