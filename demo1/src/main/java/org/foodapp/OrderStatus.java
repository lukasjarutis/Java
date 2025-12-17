package org.foodapp;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    COOKING,
    READY_FOR_PICKUP,
    PICKED_UP,
    DELIVERING,
    DELIVERED,
    CANCELED;

    public static OrderStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = normalize(value);
        OrderStatus direct = Arrays.stream(values())
                .filter(status -> status.name().equals(normalized))
                .findFirst()
                .orElse(null);

        if (direct != null) {
            return direct;
        }

        String simplified = stripDiacritics(normalized);
        if ("VEZAMAS".equalsIgnoreCase(simplified)) {
            return DELIVERING;
        }
        if ("PRISTATYTAS".equalsIgnoreCase(simplified)) {
            return DELIVERED;
        }
        return null;
    }

    public static String allowedValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    private static String normalize(String value) {
        return value.trim()
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_')
                .replace('-', '_');
    }

    private static String stripDiacritics(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}
