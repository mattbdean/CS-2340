package com.github.alphabet26;

import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Data access object for dealing with users. Assume all operations perform blocking I/O, access the
 * network, or do some other blocking operation.
 *
 * @author Matt Dean
 */
public interface UserDao {
    /** Registers a new user */
    User register(UserRegistrationInfo newUser);

    /** Attempts to find a user by its unique ID */
    User find(UUID id);

    /**
     * Attempts to login as the given user. Returns the details of the user if and only if the given
     * username/password combination is correct. Returns null otherwise.
     */
    @Nullable User login(String username, String password);
}
