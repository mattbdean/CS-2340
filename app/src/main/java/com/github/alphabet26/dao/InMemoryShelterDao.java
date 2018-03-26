package com.github.alphabet26.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.SearchRequest;
import com.github.alphabet26.model.Shelter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class InMemoryShelterDao implements ShelterDao {
    private final List<Shelter> data;

    public InMemoryShelterDao() {
        this(new LinkedList<Shelter>());
    }

    public InMemoryShelterDao(@NonNull List<Shelter> initialData) {
        this.data = new LinkedList<>(initialData);
    }

    @Override
    public List<Shelter> find() {
        return data;
    }

    @Override
    public List<Shelter> search(@Nullable String shelterName, @Nullable Gender gender, @Nullable AgeRange ageRange) {
        return search(SearchRequest.create(shelterName, gender, ageRange));
    }

    @Override
    public List<Shelter> search(@NonNull SearchRequest req) {
        if (req == null)
            throw new IllegalArgumentException("req == null");

        String shelterName = req.getName();
        Gender gender = req.getGender();
        AgeRange ageRange = req.getAgeRange();

        List<Shelter> allShelters = find();

        List<Shelter> filtered = new ArrayList<>();
        for (Shelter s : allShelters) {
            // Ignore shelterName if it's null, otherwise the shelter's name must contain
            // shelterName (ignoring case)
            boolean nameMatches = shelterName == null || s.getName().toLowerCase().contains(shelterName.toLowerCase());

            // For AgeRange and Gender, ignore if they're null or equal to ANY
            boolean ageRangeMatches = ageRange == null || ageRange == AgeRange.ANY || ageRange == s.getAgeRange();
            boolean genderMatches = gender == null || gender == Gender.ANY || gender == s.getGender();

            // Make sure everything matches
            if (genderMatches && ageRangeMatches && nameMatches) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    @Override
    @Nullable
    public Shelter update(Shelter newShelter) {
        Shelter previousInfo = null;

        for (int i = 0; i < data.size(); i++) {
            if (newShelter.getId() == data.get(i).getId()) {
                previousInfo = data.set(i, newShelter);
                break;
            }
        }

        return previousInfo;
    }

    @Nullable
    @Override
    public Shelter pluck(int id) {
        for (Shelter s : data) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }
}
