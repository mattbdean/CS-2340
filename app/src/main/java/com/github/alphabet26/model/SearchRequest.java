package com.github.alphabet26.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchRequest implements Parcelable {
    @Nullable public abstract String getName();
    @Nullable public abstract Gender getGender();
    @Nullable public abstract AgeRange getAgeRange();

    public static SearchRequest create(String newName, Gender newGender, AgeRange newAgeRange) {
        return new AutoValue_SearchRequest(newName, newGender, newAgeRange);
    }
}
