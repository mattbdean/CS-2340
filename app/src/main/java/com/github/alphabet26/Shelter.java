package com.github.alphabet26;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly
 * holds information
 * Created by Julianne Lefelhocz on 2/27/18.
 */
public final class Shelter {
    private final String shelterName;
    private final int capacity;
    private final int openBeds;
    private final float longitude;
    private final float latitude;
    private final String address;
    private final String phoneNumber;
    private final Gender gender;

    public Shelter(String shelterName, int capacity, int openBeds, float longitude, float latitude,
                   String address, String phoneNumber, Gender gender) {

        this.shelterName = shelterName;
        this.capacity = capacity;
        this.openBeds = openBeds;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getName() { return shelterName; }
    public int getCapacity() { return capacity; }
    public int getOpenBeds() { return openBeds; }
    public float getLongitude() { return longitude; }
    public float getLatitude() { return latitude; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public Gender getGender() { return gender; }
}
