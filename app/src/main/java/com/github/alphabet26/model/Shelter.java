package com.github.alphabet26.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly
 * holds information
 * Created by Julianne Lefelhocz on 2/27/18.
 */
@AutoValue
public abstract class Shelter implements Parcelable {
    public abstract int getId();
    public abstract String getName();
    public abstract String getCapacity();
    public abstract String getRestrictions();
    public abstract float getLongitude();
    public abstract float getLatitude();
    public abstract String getAddress();
    public abstract String getPhoneNumber();
    public abstract String getSpecialNotes();

    public static Shelter create(int id, String name, String capacity, String restrictions, float longitude, float latitude, String address, String phoneNumber, String specialNotes) {
        return new AutoValue_Shelter(id, name, capacity, restrictions, longitude, latitude, address, phoneNumber, specialNotes);
    }
}
