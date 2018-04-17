package com.github.alphabet26.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.UUID;

/**
 * thing
 */
@AutoValue
public abstract class User implements Model<UUID>, Parcelable {
    /**
     * getter method
     * @return thing we are getting
     */
    @Override public abstract UUID getId();
    /**
     * getter method
     * @return thing we are getting
     */
    public abstract String getName();
    /**
     * getter method
     * @return thing we are getting
     */
    public abstract String getUsername();
    /**
     * getter method
     * @return thing we are getting
     */
    public abstract String getPasswordHash();
    /**
     * getter method
     * @return thing we are getting
     */
    public abstract UserType getUserType();
    /**
     * getter method
     * @return thing we are getting
     */
    @Nullable public abstract BedClaim getCurrentClaim();

    /** Creates a new User object with everything exactly the same with the new BedClaim info
     * @param claim is the bed claim
     * @return the user who claims it */
    public User withClaim(@Nullable BedClaim claim) {
        return User.create(getId(), getName(), getUsername(), getPasswordHash(), getUserType(),
            claim);
    }

    /**
     * creates user
     * @param id id
     * @param name name
     * @param username username
     * @param passwordHash password
     * @param userType user type
     * @param bedClaim bed claim
     * @return the user
     */
    public static User create(UUID id, String name, String username, String passwordHash,
                              UserType userType, @Nullable BedClaim bedClaim) {

        return new AutoValue_User(id, name, username, passwordHash, userType, bedClaim);
    }

    /**
     * create user
     * @param info the info
     * @param passwordHash the password
     * @return the user
     */
    public static User create(UserRegistrationInfo info, String passwordHash) {
        return create(UUID.randomUUID(), info.getName(), info.getUsername(), passwordHash,
            info.getUserType(), null);
    }
}
