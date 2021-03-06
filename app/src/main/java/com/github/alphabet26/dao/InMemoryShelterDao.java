package com.github.alphabet26.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.SearchRequest;
import com.github.alphabet26.model.Shelter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ShelterDao being used in memory
 */
public final class InMemoryShelterDao extends InMemoryDao<Integer, Shelter> implements ShelterDao {
    /**
     * instantiation declaration
     */
    public InMemoryShelterDao() {
        this(new ArrayList<Shelter>());
    }

    /**
     * Instantiation declaration
     * @param initialData the initial data from list
     */
    public InMemoryShelterDao(@NonNull List<Shelter> initialData) {
        super(initialData);
    }

    @Override
    public List<Shelter> search(
        @Nullable String shelterName, @Nullable Gender gender, @Nullable AgeRange ageRange) {
        return search(SearchRequest.create(shelterName, gender, ageRange));
    }

    @Override
    public List<Shelter> search(@NonNull SearchRequest req) {
        //noinspection ConstantConditions
        if (req == null) {
            throw new IllegalArgumentException("req == null");
        }

        String shelterName = req.getName();
        Gender gender = req.getGender();
        AgeRange ageRange = req.getAgeRange();

        List<Shelter> allShelters = list();

        List<Shelter> filtered = new ArrayList<>();
        for (Shelter s : allShelters) {
            // Ignore shelterName if it's null, otherwise the shelter's name must contain
            // shelterName (ignoring case)
            boolean nameMatches = (shelterName == null) || (s.getName().toLowerCase(
                Locale.getDefault())
                .contains(shelterName.toLowerCase(Locale.getDefault())));

            // For AgeRange and Gender, ignore if they're null or equal to ANY
            boolean ageRangeMatches = (ageRange == null) ||
                (ageRange == AgeRange.ANY) || (ageRange == s.getAgeRange());
            boolean genderMatches = (gender == null) ||
                (gender == Gender.ANY) || (gender == s.getGender());

            // Make sure everything matches
            if (genderMatches && ageRangeMatches && nameMatches) {
                filtered.add(s);
            }
        }

        return filtered;
    }
}
