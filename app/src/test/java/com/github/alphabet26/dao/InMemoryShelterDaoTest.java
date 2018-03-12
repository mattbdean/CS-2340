package com.github.alphabet26.dao;

import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.Shelter;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public final class InMemoryShelterDaoTest {
    private InMemoryShelterDao dao;

    private static List<Shelter> testShelters;
    private static final int NUM_DUPLICATES = 2;

    @BeforeClass
    public static void setUpClass() {
        List<Shelter> shelters = new ArrayList<>();

        // Generate shelters with all combinations of Gender and AgeRage
        int shelterCounter = 0;
        for (int i = 0; i < NUM_DUPLICATES; i++) {
            int genderCounter = 0;
            int ageRangeCounter = 0;

            while (ageRangeCounter < AgeRange.values().length) {
                Gender g = Gender.values()[genderCounter];
                AgeRange r = AgeRange.values()[ageRangeCounter];

                shelters.add(Shelter.create(shelterCounter, "Shelter " + shelterCounter++, "<capacity>", g, r, 0, 0, "<address>", "<phone #>", "<notes>"));

                if (++genderCounter >= Gender.values().length) {
                    genderCounter = 0;
                    ageRangeCounter++;
                }
            }
        }

        testShelters = shelters;
    }

    @Test
    public void find_shouldReturnEmptyListWhenGivenNoInitialData() {
        dao = new InMemoryShelterDao();
        assertThat(dao.find()).isEmpty();
    }

    @Test
    public void find_shouldReturnInitialData() {
        dao = new InMemoryShelterDao(testShelters);

        // find() should return data that is equivalent to
        assertThat(dao.find()).isEqualTo(testShelters);
        assertThat(dao.find()).isNotSameAs(testShelters);
    }

    @Test
    public void search_shouldReturnEverythingWhenGivenNoFilters() {
        dao = new InMemoryShelterDao(testShelters);

        assertThat(dao.search(null, null, null)).isEqualTo(dao.find());
    }

    @Test
    public void search_shouldFilterNamesStartWithIgnoreCase() {
        dao = new InMemoryShelterDao(testShelters);
        // All shelters are named "Shelter X"
        assertThat(dao.search("shel", null, null)).isEqualTo(dao.find());

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
        assertThat(dao.search(null, Gender.ANY, AgeRange.ANY)).isEqualTo(dao.find());
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
        assertThat(dao.search(null, null, AgeRange.ANY)).isEqualTo(dao.find());
    }

    @Test
    public void search_shouldWorkWithAllParameters() {
        dao = new InMemoryShelterDao(testShelters);
        // There are exactly NUM_DUPLICATES shelters that restrict gender to male and age range to
        // children
        assertThat(dao.search("shelter ", Gender.MALE, AgeRange.CHILDREN)).hasSize(NUM_DUPLICATES);
    }
}
