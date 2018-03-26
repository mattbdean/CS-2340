package com.github.alphabet26.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly holds information
 */
@AutoValue
public abstract class Shelter implements Parcelable {
    public abstract int getId();
    public abstract String getName();
    public abstract String getCapacity();
    public abstract Gender getGender();
    public abstract AgeRange getAgeRange();
    public abstract float getLongitude();
    public abstract float getLatitude();
    public abstract String getAddress();
    public abstract String getPhoneNumber();
    public abstract String getSpecialNotes();
    public abstract int getAvailableBeds();

    public static Shelter create(int id, String name, String capacity, Gender gender, AgeRange ageRange, float longitude, float latitude, String address, String phoneNumber, String specialNotes, int availableBeds) {
        return new AutoValue_Shelter(id, name, capacity, gender, ageRange, longitude, latitude, address, phoneNumber, specialNotes, availableBeds);
    }

    /**
     * Returns a new Shelter object with everything the same except for the number of available beds
     */
    public Shelter withAvailableBeds(int newAvailableBeds) {
        return create(getId(), getName(), getCapacity(), getGender(), getAgeRange(), getLongitude(),
            getLatitude(), getAddress(), getPhoneNumber(), getSpecialNotes(), newAvailableBeds);
    }
}
