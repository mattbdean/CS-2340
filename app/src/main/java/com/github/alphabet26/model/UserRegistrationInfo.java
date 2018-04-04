package com.github.alphabet26.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserRegistrationInfo {
    public abstract String getName();
    public abstract String getUsername();
    public abstract String getPlaintextPassword();
    public abstract UserType getUserType();

    public static UserRegistrationInfo create(String newName, String newUsername, String newPlaintextPassword, UserType newUserType) {
        return new AutoValue_UserRegistrationInfo(newName, newUsername, newPlaintextPassword, newUserType);
    }
}
