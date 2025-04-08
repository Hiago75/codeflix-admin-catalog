package com.codeflix.admin.catalog;

import com.codeflix.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.codeflix.admin.catalog.infrastructure.category.persistance.CategoryRepository;
import com.codeflix.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import com.codeflix.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));
    }

    public void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}