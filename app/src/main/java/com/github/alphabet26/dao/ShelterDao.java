package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.Shelter;

import java.util.List;

/**
 * Data access object for working with homeless shelters. Assume all operations perform blocking
 * I/O.
 */
public interface ShelterDao {
    // TODO geolocation
    /** Finds a list of Shelters */
    List<Shelter> find();

    /**
     * Searches for Shelters. If all parameters are null, this method functions the same way as
     * {@link #find}. The gender and age range parameters must match exactly. How the shelter name
     * parameter is up to the individual implementation.
     */
    List<Shelter> search(@Nullable String shelterName, @Nullable Gender gender, @Nullable AgeRange ageRange);
}
