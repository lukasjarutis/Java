package org.foodapp;

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
        return Arrays.stream(values())
                .filter(status -> status.name().equals(normalized))
                .findFirst()
                .orElse(null);
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
}
