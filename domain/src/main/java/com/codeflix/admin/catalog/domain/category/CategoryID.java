package com.codeflix.admin.catalog.domain.category;

import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
