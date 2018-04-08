package com.github.alphabet26.model;

import com.google.auto.value.AutoValue;

/**
 * user registration info represents a user's info for registration
 */
@AutoValue
public abstract class UserRegistrationInfo {
    /**
     * name getter method
     * @return name
     */
    public abstract String getName();

    /**
     * getter method for username
     * @return username
     */
    public abstract String getUsername();

    /**
     * getter method for plain text password
     * @return password
     */
    public abstract String getPlaintextPassword();

    /**
     * getter method for user type
     * @return user type
     */
    public abstract UserType getUserType();

    /**
     * Creates a user registration info instance
     * @param newName new name
     * @param newUsername new username
     * @param newPlaintextPassword new password
     * @param newUserType new user type
     * @return the instance of the class
     */
    public static UserRegistrationInfo create(String newName, String newUsername,
                                              String newPlaintextPassword, UserType newUserType) {
        return new AutoValue_UserRegistrationInfo(newName, newUsername, newPlaintextPassword,
            newUserType);
    }
}
