package com.codeflix.admin.catalog.infrastructure.category.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

}
