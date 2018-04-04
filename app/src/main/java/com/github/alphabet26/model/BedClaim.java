package com.github.alphabet26.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Represents a User's claim on a bed at a specific shelter
 */
@AutoValue
public abstract class BedClaim implements Parcelable {
    /**
     * getter method shelter id
     * @return the shelter id
     */
    public abstract int getShelterId();

    /**
     * getter method for bed count
     * @return the bed count
     */
    public abstract int getBedCount();

    public static BedClaim create(int newShelterId, int newBedCount) {
        return new AutoValue_BedClaim(newShelterId, newBedCount);
    }
}
