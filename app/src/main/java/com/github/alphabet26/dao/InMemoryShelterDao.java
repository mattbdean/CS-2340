package com.github.alphabet26.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
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
        List<Shelter> allShelters = find();

        List<Shelter> filtered = new ArrayList<>();
        for (Shelter s : allShelters) {
            boolean nameMatches = shelterName == null || s.getName().toLowerCase().startsWith(shelterName.toLowerCase());
            if (s.getGender() == gender && s.getAgeRange() == ageRange && nameMatches) {
                filtered.add(s);
            }
        }

        return filtered;
    }
}
