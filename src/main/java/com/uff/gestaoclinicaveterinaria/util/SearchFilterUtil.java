package com.uff.gestaoclinicaveterinaria.util;

import java.text.Normalizer;

public final class SearchFilterUtil {

    private SearchFilterUtil() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }

        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase()
                .trim();
    }

    public static boolean startsWithNormalized(String origin, String normalizedSearch) {
        if (normalizedSearch == null || normalizedSearch.isEmpty()) {
            return true;
        }

        return normalize(origin).startsWith(normalizedSearch);
    }
}