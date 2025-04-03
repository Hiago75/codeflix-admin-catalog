package com.codeflix.admin.catalog.domain.castmember;

import com.codeflix.admin.catalog.domain.Identifier;
import com.codeflix.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CastMemberID unique() {
        return CastMemberID.from(IdUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
