package com.github.alphabet26.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * search requets
 */
@AutoValue
public abstract class SearchRequest implements Parcelable {
    /**
     * getter method
     * @return thing we are getting
     */
    @Nullable public abstract String getName();
    /**
     * getter method
     * @return thing we are getting
     */
    @Nullable public abstract Gender getGender();
    /**
     * getter method
     * @return thing we are getting
     */
    @Nullable public abstract AgeRange getAgeRange();

    /**
     * creates a search request
     * @param newName new name
     * @param newGender new gender
     * @param newAgeRange new age range
     * @return the search request
     */
    public static SearchRequest create(String newName, Gender newGender, AgeRange newAgeRange) {
        return new AutoValue_SearchRequest(newName, newGender, newAgeRange);
    }
}
