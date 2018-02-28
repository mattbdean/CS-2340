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
        shelters.add(Shelter.create(5, "Test1", "15", "none", 100, 100, "addy", "test", "(123) 456-7890"));
        shelters.add(Shelter.create(6, "Test2", "10", "none", 100, 100, "addy", "test", "(098) 765-4321"));

        dao = new InMemoryShelterDao(shelters);

        // find() should return data that is equivalent to
        assertThat(dao.find()).isEqualTo(shelters);
        assertThat(dao.find()).isNotSameAs(shelters);
    }
}
