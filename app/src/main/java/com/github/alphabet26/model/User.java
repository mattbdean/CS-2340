package com.github.alphabet26.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.UUID;

@AutoValue
public abstract class User implements Parcelable {
    public abstract UUID getId();
    public abstract String getName();
    public abstract String getUsername();
    public abstract String getPasswordHash();
    public abstract UserType getUserType();
    @Nullable public abstract BedClaim getCurrentClaim();

    /** Creates a new User object with everything exactly the same with the new BedClaim info */
    public User withClaim(@Nullable BedClaim claim) {
        return User.create(getId(), getName(), getUsername(), getPasswordHash(), getUserType(), claim);
    }

    public static User create(UUID id, String name, String username, String passwordHash,
                              UserType userType, @Nullable BedClaim bedClaim) {

        return new AutoValue_User(id, name, username, passwordHash, userType, bedClaim);
    }

    public static User create(UserRegistrationInfo info, String passwordHash) {
        return create(UUID.randomUUID(), info.getName(), info.getUsername(), passwordHash, info.getUserType(), null);
    }
}
