package com.codeflix.admin.catalog.infrastructure.video.persistence;

import com.codeflix.admin.catalog.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {
    @Query("""
           SELECT DISTINCT new com.codeflix.admin.catalog.domain.video.VideoPreview(
                v.id as id,
                v.title as title,
                v.description as description,
                v.createdAt as createdAt,
                v.updatedAt as updatedAt
           )
           FROM Video v
                LEFT JOIN v.castMembers members
                LEFT JOIN v.categories categories
                LEFT JOIN v.genres genres
           WHERE
                (:terms IS NULL OR UPPER(v.title) LIKE :terms)
           AND
                (:castMembers IS NULL OR members.id.castMemberId IN :castMembers)
           AND
                (:categories IS NULL OR categories.id.categoryId IN :categories)
           AND
                (:genres IS NULL OR genres.id.genreId IN :genres)
           """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("castMembers") Set<String> castMembers,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            Pageable page
    );
}
