package com.github.alphabet26.model;

/** A model to be used by a {@link com.github.alphabet26.dao.Dao} */
public interface Model<T> {
    /**
     * getter method for id
     * @return id
     */
    T getId();
}
