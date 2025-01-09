package com.example.demosignapp.domain.common;

public class EnumUtil {

    public static <T extends Enum<T>> T fromString(Class<T> enumType, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null for enum " + enumType.getSimpleName());
        }
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + enumType.getSimpleName() + ": " + value, e);
        }
    }
}
