package com.codeflix.admin.catalog.domain.genre;

import com.codeflix.admin.catalog.domain.AggregateRoot;
import com.codeflix.admin.catalog.domain.category.CategoryID;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> aCategoriesList,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.categories = aCategoriesList;
        this.active = isActive;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;

        final var notification = Notification.create();
        validate(notification);

        if(notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> aCategoriesList,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
       return new Genre(anId, aName, isActive, aCategoriesList, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt, aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public static Genre newGenre( final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;

        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
