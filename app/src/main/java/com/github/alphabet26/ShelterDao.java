package com.github.alphabet26;

import java.util.List;

/**
 * Data access object for working with homeless shelters. Assume all operations perform blocking
 * I/O.
 */
public interface ShelterDao {
    // TODO geolocation
    /** Finds a list of Shelters */
    List<Shelter> find();
}
