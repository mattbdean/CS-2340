package com.github.alphabet26;

import android.support.annotation.NonNull;

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
