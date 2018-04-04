package com.github.alphabet26.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.SearchRequest;
import com.github.alphabet26.model.Shelter;

import java.util.List;

/**
 * Data access object for working with homeless shelters. Assume all operations perform blocking
 * I/O.
 */
public interface ShelterDao extends Dao<Integer, Shelter> {
    /**
     * Searches for Shelters. If all parameters are null, this method functions the same way as
     * {@link #list}. The gender and age range parameters must match exactly. How the shelter name
     * parameter is up to the individual implementation. To exclude gender matching, pass null to
     * `gender`. Both {@link AgeRange#ANY} and `null` provide the same functionality for age range
     * filtering.
     */
    List<Shelter> search(@NonNull SearchRequest req);

    /**
     * Creates a SearchRequest out of the given parameters and passes it to
     * {@link #search(SearchRequest)}.
     */
    List<Shelter> search(
        @Nullable String shelterName, @Nullable Gender gender, @Nullable AgeRange ageRange);

    @Nullable @Override Shelter update(Shelter newInfo);
}
