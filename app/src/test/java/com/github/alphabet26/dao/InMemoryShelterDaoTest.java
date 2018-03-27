package com.github.alphabet26.dao;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.Shelter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public final class InMemoryShelterDaoTest {
    private static final int NUM_DUPLICATES = 2;
    private InMemoryShelterDao dao;

    private List<Shelter> testShelters;

    @Before
    public void setUp() {
        testShelters = createTestShelters(NUM_DUPLICATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void search_shouldNotAcceptNullSearchRequests() {
        //noinspection ConstantConditions
        dao = new InMemoryShelterDao();
        dao.search(null);
    }

    @Test
    public void search_shouldReturnEverythingWhenGivenNoFilters() {
        dao = new InMemoryShelterDao(testShelters);

        assertThat(dao.search(null, null, null)).isEqualTo(dao.list());
    }

    @Test
    public void search_shouldFilterNamesStartWithIgnoreCase() {
        dao = new InMemoryShelterDao(testShelters);
        // All shelters are named "Shelter X"
        assertThat(dao.search("shel", null, null)).isEqualTo(dao.list());

        // Only 1 shelter should have a "12" in it
        assertThat(dao.search("12", null, null)).hasSize(1);
    }

    @Test
    public void search_shouldFilterGender() {
        dao = new InMemoryShelterDao(testShelters);
        List<Shelter> filtered = dao.search(null, Gender.MALE, null);
        for (Shelter s : filtered) {
            assertThat(s.getGender()).isEqualTo(Gender.MALE);
        }
    }

    @Test
    public void search_shouldNotFilterAgeRageWhenAny() {
        dao = new InMemoryShelterDao(testShelters);
        assertThat(dao.search(null, Gender.ANY, AgeRange.ANY)).isEqualTo(dao.list());
    }

    @Test
    public void search_shouldFilterAgeRange() {
        dao = new InMemoryShelterDao(testShelters);
        List<Shelter> filtered = dao.search(null, null, AgeRange.CHILDREN);
        for (Shelter s : filtered) {
            assertThat(s.getAgeRange()).isEqualTo(AgeRange.CHILDREN);
        }
    }

    @Test
    public void search_shouldNotFilterAgeRangeWhenAny() {
        dao = new InMemoryShelterDao(testShelters);
        assertThat(dao.search(null, null, AgeRange.ANY)).isEqualTo(dao.list());
    }

    @Test
    public void search_shouldWorkWithAllParameters() {
        dao = new InMemoryShelterDao(testShelters);
        // There are exactly NUM_DUPLICATES shelters that restrict gender to male and age range to
        // children
        assertThat(dao.search("shelter ", Gender.MALE, AgeRange.CHILDREN)).hasSize(NUM_DUPLICATES);
    }


    /** Equivalent to {@code createTestShelters(1)} */
    static List<Shelter> createTestShelters() { return createTestShelters(1); }

    /**
     * Creates a List of Shelters where each shelter has a unique AgeRange and Gender. Shelters are
     * named Shelter 0, Shelter 1, and so on. Other features like capacity and address are the same
     * for every Shelter. The number of available beds is equal to its ID.
     * @param numDuplicateFeatures The amount of Shelters with a specific AgeRange/Gender
     *                             combination.
     */
    static List<Shelter> createTestShelters(int numDuplicateFeatures) {
        List<Shelter> shelters = new ArrayList<>();

        // Generate shelters with all combinations of Gender and AgeRage
        int shelterCounter = 0;
        for (int i = 0; i < numDuplicateFeatures; i++) {
            int genderCounter = 0;
            int ageRangeCounter = 0;

            while (ageRangeCounter < AgeRange.values().length) {
                Gender g = Gender.values()[genderCounter];
                AgeRange r = AgeRange.values()[ageRangeCounter];

                shelters.add(Shelter.create(shelterCounter, "Shelter " + shelterCounter,
                    69, g, r, 0, 0, "<address>", "<phone #>", "<notes>", shelterCounter));

                shelterCounter++;

                if (++genderCounter >= Gender.values().length) {
                    genderCounter = 0;
                    ageRangeCounter++;
                }
            }
        }

        return shelters;
    }
}
