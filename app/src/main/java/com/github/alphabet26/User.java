package com.github.alphabet26;

import com.google.auto.value.AutoValue;

import java.util.UUID;

@AutoValue
public abstract class User {
    public abstract UUID getId();
    public abstract String getName();
    public abstract String getUsername();
    public abstract String getPasswordHash();
    public abstract UserType getUserType();

    public static User create(UUID id, String name, String username, String passwordHash, UserType userType) {
        return new AutoValue_User(id, name, username, passwordHash, userType);
    }

    public static User create(UserRegistrationInfo info, String passwordHash) {
        return create(UUID.randomUUID(), info.getName(), info.getUsername(), passwordHash, info.getUserType());
    }
}
