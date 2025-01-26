package com.codeflix.admin.catalog.infrastructure.category;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.codeflix.admin.catalog.domain.pagination.Pagination;
import com.codeflix.admin.catalog.infrastructure.category.persistance.CategoryJpaEntity;
import com.codeflix.admin.catalog.infrastructure.category.persistance.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository aRepository) {
        this.repository = Objects.requireNonNull(aRepository);
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {
        final var anIdValue = anId.getValue();

        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }
}
