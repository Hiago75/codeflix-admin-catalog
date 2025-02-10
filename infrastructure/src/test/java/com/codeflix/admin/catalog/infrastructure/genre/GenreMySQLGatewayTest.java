package com.codeflix.admin.catalog.infrastructure.genre;

import com.codeflix.admin.catalog.MySQLGatewayTest;
import com.codeflix.admin.catalog.infrastructure.category.CategoryMySQLGateway;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }
}
