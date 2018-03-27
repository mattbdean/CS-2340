package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.Model;

import java.util.List;

/**
 * A generic Data Access Object. Assume all operations are blocking.
 * @param <T> Model type
 * @param <I> The type of the model's ID
 */
public interface Dao<I, T extends Model<I>> {
    /**
     * Attempts to find a particular piece of data given its ID. Returns null if there is no model
     * associated with that ID.
     */
    @Nullable T find(I id);

    /**
     * Returns a list of all models in no particular order
     */
    List<T> list();

    /**
     * Finds a model that has the same ID as the given model and replaces the existing data with the
     * given data.
     *
     * @throws IllegalArgumentException If there is existing model with the same ID as the model
     * given
     * @return The data that was replaced
     */
    T update(T current);
}
