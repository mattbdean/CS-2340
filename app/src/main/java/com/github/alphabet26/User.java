package com.github.alphabet26;

import java.util.UUID;

public final class User {
    private final UUID id;
    private final String name;
    private final String username;
    private final String passwordHash;
    private final UserType userType;

    public User(UUID id, String name, String username, String passwordHash, UserType userType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = userType;
    }

    public User(UserRegistrationInfo info, String passwordHash) {
        this(UUID.randomUUID(), info.getName(), info.getUsername(), passwordHash, info.getUserType());
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public UserType getUserType() { return userType; }
}
