package com.github.alphabet26.dao;

import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;
import com.github.alphabet26.model.UserType;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

public class InMemoryUserDaoTest {
    private InMemoryUserDao dao;

    @Before
    public void setUp() {
        dao = new InMemoryUserDao();
    }

    @Test
    public void register_shouldReturnTheIdOfTheNewUser() {
        User result = dao.register(new UserRegistrationInfo("Name", "username", "password", UserType.USER));
        assertThat(result).isNotNull();
    }

    @Test
    public void register_shouldReturnDifferentUUIDsEveryTime() {
        UserRegistrationInfo u1 = new UserRegistrationInfo("Name1", "username1", "password2", UserType.USER);
        UserRegistrationInfo u2 = new UserRegistrationInfo("Name2", "username2", "password2", UserType.ADMIN);

        assertThat(dao.register(u1)).isNotEqualTo(dao.register(u2));
    }

    @Test
    public void register_shouldNotStorePasswordsInPlaintext() {
        User fromStore = dao.find(dao.register(new UserRegistrationInfo("Name", "username", "password", UserType.USER)).getId());
        assertThat(fromStore).isNotNull();
        assertThat(fromStore.getPasswordHash()).isNotEqualTo("password2");
    }

    @Test
    public void register_shouldThrowIllegalArgumentExceptionIfUsernameExists() {
        // The only thing the same between these two are the usernames
        UserRegistrationInfo u1 = new UserRegistrationInfo("Name1", "username", "password2", UserType.USER);
        UserRegistrationInfo u2 = new UserRegistrationInfo("Name2", "username", "password2", UserType.ADMIN);
        dao.register(u1);

        try {
            dao.register(u2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void find_shouldReturnTheUserWithTheGivenId() {
        User newUser = dao.register(new UserRegistrationInfo("Name", "username", "password", UserType.USER));
        User user = dao.find(newUser.getId());

        assertThat(newUser).isNotNull();
        assertThat(user).isNotNull();
        assertThat(newUser).isEqualTo(user);
    }

    @Test
    public void find_shouldReturnNullWhenNoUserCouldBeFound() {
        assertThat(dao.find(UUID.randomUUID())).isNull();
    }

    @Test
    public void login_shouldReturnNullWhenGivenInvalidCredentials() {
        assertThat(dao.login("username", "password")).isNull();
    }

    @Test
    public void login_shouldReturnUserInfoOnSuccess() {
        String username = "username", password = "password";
        User newUser = dao.register(
                new UserRegistrationInfo("test user", username, password, UserType.USER));

        assertThat(dao.login(username, password)).isEqualTo(newUser);
    }
}
