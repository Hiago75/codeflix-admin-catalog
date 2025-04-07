package com.codeflix.admin.catalog.infrastructure.utils;

public final class SqlUtils {
    private SqlUtils() {}

    public static String like(String term) {
        if (term == null) return null;

        return "%" + term + "%";
    }
}
