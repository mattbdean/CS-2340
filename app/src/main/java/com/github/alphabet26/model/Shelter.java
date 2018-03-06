package com.github.alphabet26.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly holds information
 * Created by Julianne Lefelhocz on 2/27/18.
 */
@AutoValue
public abstract class Shelter implements Parcelable {
    public abstract int getId();
    public abstract String getName();
    public abstract String getCapacity();
    @Nullable public abstract Gender getGender();
    public abstract AgeRange getAgeRange();
    public abstract float getLongitude();
    public abstract float getLatitude();
    public abstract String getAddress();
    public abstract String getPhoneNumber();
    public abstract String getSpecialNotes();

    public static Shelter create(int id, String name, String capacity, Gender gender, AgeRange ageRange, float longitude, float latitude, String address, String phoneNumber, String specialNotes) {
        return new AutoValue_Shelter(id, name, capacity, gender, ageRange, longitude, latitude, address, phoneNumber, specialNotes);
    }
}
