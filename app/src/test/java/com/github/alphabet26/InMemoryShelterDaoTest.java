package com.github.alphabet26;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public final class InMemoryShelterDaoTest {
    private InMemoryShelterDao dao;

    @Test
    public void find_shouldReturnEmptyListWhenGivenNoInitialData() {
        dao = new InMemoryShelterDao();
        assertThat(dao.find()).isEmpty();
    }

    @Test
    public void find_shouldReturnInitialData() {
        List<Shelter> shelters = new ArrayList<>();
        shelters.add(new Shelter("Test", 5, 2, 100, 100, "addy", "test", Gender.MALE));
        shelters.add(new Shelter("Test2", 10, 5, 100, 100, "addy", "test", Gender.FEMALE));

        dao = new InMemoryShelterDao(shelters);

        // find() should return data that is equivalent to
        assertThat(dao.find()).isEqualTo(shelters);
        assertThat(dao.find()).isNotSameAs(shelters);
    }
}
