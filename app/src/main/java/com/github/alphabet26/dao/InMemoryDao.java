package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class InMemoryDao<I, T extends Model<I>> implements Dao<I, T> {
    protected List<T> data;

    protected InMemoryDao(List<T> data) {
        this.data = new ArrayList<>(data);
    }

    @Nullable
    @Override
    public T find(I id) {
        for (T element : data) {
            if (element.getId().equals(id)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public List<T> list() {
        // Copy the data so we don't let users of this method modify our internal data
        return new ArrayList<>(data);
    }

    @Override
    public T update(T newModel) {
        T previousInfo = null;

        for (int i = 0; i < data.size(); i++) {
            if (newModel.getId().equals(data.get(i).getId())) {
                previousInfo = data.set(i, newModel);
                break;
            }
        }

        if (previousInfo == null) {
            throw new IllegalArgumentException("Cannot update model with ID " + newModel.getId() +
                ", model doesn't exist");
        }

        return previousInfo;
    }
}
