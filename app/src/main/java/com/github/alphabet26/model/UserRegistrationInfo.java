package com.github.alphabet26.model;

// TODO use autovalue
public final class UserRegistrationInfo {
    private final String name;
    private final String username;
    private final String plaintextPassword;
    private final UserType type;

    public UserRegistrationInfo(String name, String username, String plaintextPassword, UserType type) {
        this.name = name;
        this.username = username;
        this.plaintextPassword = plaintextPassword;
        this.type = type;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPlaintextPassword() { return plaintextPassword; }
    public UserType getUserType() { return type; }
}
