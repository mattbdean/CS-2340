package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.BedClaim;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;

import java.util.UUID;

/**
 * Data access object for dealing with users. Assume all operations perform blocking I/O, access the
 * network, or do some other blocking operation.
 *
 * @author Matt Dean
 */
public interface UserDao extends Dao<UUID, User> {
    /** Registers a new user */
    User register(UserRegistrationInfo newUser);

    /**
     * Attempts to login as the given user. Returns the details of the user if and only if the given
     * username/password combination is correct. Returns null otherwise.
     */
    @Nullable User login(String username, String password);

    /**
     * Attempts to claim a bed for a user with the specified ID
     *
     * @throws IllegalArgumentException If either the user ID or shelter ID is not valid, if the
     * shelter doesn't have enough beds, if the number of requested beds is not positive, or if the
     * user already has a claim on another bed.
     *
     * @return The user's new BedClaim
     */
    BedClaim claimBeds(ShelterDao shelterDao, UUID userId, int shelterId, int beds);

    void releaseClaim(ShelterDao shelterDao, UUID userId);
}
