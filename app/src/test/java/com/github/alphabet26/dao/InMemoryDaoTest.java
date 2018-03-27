package com.github.alphabet26.dao;

import com.github.alphabet26.model.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

public class InMemoryDaoTest {
    private MockDao dao;

    @Before
    public void setUp() {
        dao = new MockDao();
    }

    @Test
    public void find_shouldReturnNullGivenNoData() {
        // Test multiple IDs
        for (int i = 0; i < 10; i++) {
            assertThat(dao.find(i)).isNull();
        }
    }

    @Test
    public void find_shouldReturnDataWhenItHasTheId() {
        final int count = 5;
        dao.addEntries(count);

        for (int i = 0; i < count; i++) {
            assertThat(dao.find(i)).isEqualTo(new Foo(i));
        }
    }

    @Test
    public void list_shouldReturnACopy() {
        assertThat(dao.list()).isNotSameAs(dao.data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNoModelWithGivenIdExists() {
        dao.update(new Foo(0));
    }

    @Test
    public void update_shouldUpdateTheModelWhenIdExists() {
        final int id = 1;
        dao.addEntries(id + 1);

        Foo newFoo = new Foo(id, 999);
        dao.update(newFoo);

        assertThat(dao.find(id)).isEqualTo(newFoo);
    }

    private static final class Foo implements Model<Integer> {
        private int id;
        private int bar;
        Foo(int id) { this(id, id * 2); }
        Foo(int id, int bar) { this.id = id; this.bar = bar; }
        @Override public Integer getId() { return id; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Foo foo = (Foo) o;

            return id == foo.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    private static class MockDao extends InMemoryDao<Integer, Foo> {
        private int lastEntry = 0;

        MockDao() {
            super(new ArrayList<Foo>());
        }

        void addEntries(int count) {
            for (int i = 0; i < count; i++) {
                data.add(new Foo(lastEntry++));
            }
        }
    }
}

