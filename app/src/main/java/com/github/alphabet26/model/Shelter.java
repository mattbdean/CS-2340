package com.github.alphabet26.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly holds information
 */
@SuppressWarnings("ClassWithTooManyDependents")
@AutoValue
public abstract class Shelter implements Model<Integer>, Parcelable {
    @Override public abstract Integer getId();

    /**
     * getter method for name
     * @return name
     */
    public abstract String getName();
    /**
     * getter method for Capacity
     * @return Capacity
     */
    public abstract int getCapacity();
    /**
     * getter method for gender
     * @return gender
     */
    public abstract Gender getGender();
    /**
     * getter method for age range
     * @return age range
     */
    public abstract AgeRange getAgeRange();
    /**
     * getter method for longitude
     * @return longitude
     */
    public abstract float getLongitude();
    /**
     * getter method for latitude
     * @return latitude
     */
    public abstract float getLatitude();
    /**
     * getter method for address
     * @return address
     */
    public abstract String getAddress();
    /**
     * getter method for phone number
     * @return phone number
     */
    public abstract String getPhoneNumber();

    /**
     * getter method for notes
     * @return notes
     */
    @Json(name = "notes") public abstract String getSpecialNotes();

    /**
     * getter method for available beds
     * @return available beds
     */
    public abstract int getAvailableBeds();

    /**
     * Creates a shelter
     * @param id id
     * @param name name
     * @param capacity capacity
     * @param gender gender
     * @param ageRange ageRange
     * @param longitude longitude
     * @param latitude latitude
     * @param address address
     * @param phoneNumber phoneNumber
     * @param specialNotes specialNotes
     * @param availableBeds availableBeds
     * @return Shelter
     */
    public static Shelter create(int id, String name, int capacity, Gender gender,
                                 AgeRange ageRange, float longitude, float latitude,
                                 String address, String phoneNumber, String specialNotes,
                                 int availableBeds) {
        return new AutoValue_Shelter(id, name, capacity, gender, ageRange, longitude, latitude,
            address, phoneNumber, specialNotes, availableBeds);
    }

    /**
     * creates shelter from json
     * @param moshi json serializer
     * @return json
     */
    public static JsonAdapter<Shelter> jsonAdapter(Moshi moshi) {
        return new AutoValue_Shelter.MoshiJsonAdapter(moshi);
    }

    /**
     * Returns a new Shelter object with everything the same except for the number of available beds
     * @param newAvailableBeds number of new available beds
     * @return shelter
     */
    public Shelter withAvailableBeds(int newAvailableBeds) {
        return create(getId(), getName(), getCapacity(), getGender(), getAgeRange(), getLongitude(),
            getLatitude(), getAddress(), getPhoneNumber(), getSpecialNotes(), newAvailableBeds);
    }
}
