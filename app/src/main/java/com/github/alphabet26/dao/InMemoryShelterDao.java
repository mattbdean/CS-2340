package com.github.alphabet26.dao;

import android.support.annotation.NonNull;

import com.github.alphabet26.model.Shelter;

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
}
