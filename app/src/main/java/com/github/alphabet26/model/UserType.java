package com.github.alphabet26.model;

/**
 * The different user types.
 */

public enum UserType {
    USER ("User"),
    ADMIN ("Administrator"),
    SHELTER_WORKER ("Shelter Worker");

    private final String value;

    UserType(final String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public String toString() { return value; }
}
