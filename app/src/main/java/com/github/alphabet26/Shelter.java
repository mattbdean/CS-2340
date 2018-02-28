package com.github.alphabet26;

/**
 * Shelter is a representation of a homeless shelter in the real world, it mainly
 * holds information
 * Created by Julianne Lefelhocz on 2/27/18.
 */
public final class Shelter {
    private final int id;
    private final String shelterName;
    private final String capacity;
    private final String restrictions;
    private final float longitude;
    private final float latitude;
    private final String address;
    private final String phoneNumber;
    private final String specialNotes;
//    private final int openBeds;
//    private final Gender gender;

    public Shelter(int id, String shelterName, String capacity, String restrictions, float longitude,
                   float latitude, String address, String specialNotes, String phoneNumber) {

        this.id = id;
        this.shelterName = shelterName;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.specialNotes = specialNotes;
        this.phoneNumber = phoneNumber;
//        this.openBeds = openBeds;
//        this.gender = gender;
    }

    public int getId() { return id; }
    public String getName() { return shelterName; }
    public String getCapacity() { return capacity; }
    public String getRestrictions() { return restrictions; }
    public float getLongitude() { return longitude; }
    public float getLatitude() { return latitude; }
    public String getAddress() { return address; }
    public String getSpecialNotes() { return specialNotes; }
    public String getPhoneNumber() { return phoneNumber; }
//    public int getOpenBeds() { return openBeds; }
//    public Gender getGender() { return gender; }
}
