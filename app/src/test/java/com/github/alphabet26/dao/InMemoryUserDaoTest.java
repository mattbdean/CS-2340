package com.github.alphabet26.dao;

import com.github.alphabet26.model.BedClaim;
import com.github.alphabet26.model.Shelter;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;
import com.github.alphabet26.model.UserType;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

public class InMemoryUserDaoTest {
    /**
     * A UserRegistrationInfo object to use when you don't really care about the particulars of the
     * user, rather that a user is to be registered.
     */
    private static final UserRegistrationInfo REG_INFO =
        UserRegistrationInfo.create("foo@bar.com", "username", "password", UserType.USER);

    private InMemoryUserDao dao;
    private ShelterDao shelterDao;

    @Before
    public void setUp() {
        dao = new InMemoryUserDao();
        shelterDao = new InMemoryShelterDao(InMemoryShelterDaoTest.createTestShelters());
    }

    @Test
    public void register_shouldReturnTheIdOfTheNewUser() {
        User result = dao.register(REG_INFO);
        assertThat(result).isNotNull();
    }

    @Test
    public void register_shouldReturnDifferentUUIDsEveryTime() {
        UserRegistrationInfo u1 = UserRegistrationInfo.create("foo@bar.com", "username1", "password2", UserType.USER);
        UserRegistrationInfo u2 = UserRegistrationInfo.create("baz@qux.com", "username2", "password2", UserType.ADMIN);

        assertThat(dao.register(u1)).isNotEqualTo(dao.register(u2));
    }

    @Test
    public void register_shouldNotStorePasswordsInPlaintext() {
        User fromStore = dao.find(dao.register(UserRegistrationInfo.create("foo@bar.com", "username", "password", UserType.USER)).getId());
        assertThat(fromStore).isNotNull();
        assertThat(fromStore.getPasswordHash()).isNotEqualTo("password");
    }

    @Test
    public void register_shouldThrowIllegalArgumentExceptionIfUsernameExists() {
        // The only thing the same between these two are the usernames
        UserRegistrationInfo u1 = UserRegistrationInfo.create("foo@bar.com", "username", "password1", UserType.USER);
        UserRegistrationInfo u2 = UserRegistrationInfo.create("baz@qux.com", "username", "password2", UserType.ADMIN);
        dao.register(u1);

        try {
            dao.register(u2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void login_shouldReturnNullWhenGivenInvalidCredentials() {
        assertThat(dao.login("username", "password")).isNull();
    }

    @Test
    public void login_shouldReturnUserInfoOnSuccess() {
        String username = "username", password = "password";
        User newUser = dao.register(REG_INFO);
        assertThat(dao.login(username, password)).isEqualTo(newUser);
    }

    @Test
    public void claimBeds_shouldUpdateTheUserAndShelterModelOnSuccess() {
        List<Shelter> allShelters = shelterDao.list();
        Shelter beforeShelter = allShelters.get(allShelters.size() - 1);

        User beforeUser = dao.register(REG_INFO);
        assertThat(beforeUser.getCurrentClaim()).isNull();

        final int beds = 3;
        final BedClaim expectedClaim = BedClaim.create(beforeShelter.getId(), beds);

        // Make sure the returned claim is what we expected it to be
        BedClaim claim = dao.claimBeds(shelterDao, beforeUser.getId(), beforeShelter.getId(), beds);
        assertThat(claim).isEqualTo(expectedClaim);

        // Make sure things were updated in the model
        User afterUser = dao.find(beforeUser.getId());
        assertThat(afterUser).isNotNull();
        assertThat(afterUser.getCurrentClaim()).isEqualTo(expectedClaim);

        Shelter afterShelter = shelterDao.find(beforeShelter.getId());
        assertThat(afterShelter).isNotNull();
        assertThat(afterShelter.getAvailableBeds()).isEqualTo(beforeShelter.getAvailableBeds() - beds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void claimBeds_shouldThrowWhenUserIdIsNotValid() {
        dao.claimBeds(shelterDao, UUID.randomUUID(), shelterDao.list().get(3).getId(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void claimBeds_shouldThrowWhenShelterIdIsNotValid() {
        // Use shelterDao.find().size() because there are N shelters whose IDs range from 0 to N - 1
        dao.claimBeds(shelterDao, dao.register(REG_INFO).getId(), shelterDao.list().size(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void claimBeds_shouldThrowWhenShelterDoesNotHaveEnoughBeds() {
        // In our test data, each shelter has the same number of available beds as its ID, so
        // shelter 0 has 0 beds left.
        dao.claimBeds(shelterDao, dao.register(REG_INFO).getId(), shelterDao.list().get(0).getId(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void claimBeds_shouldThrowWhenUserAlreadyHasClaim() {
        UUID userId = dao.register(REG_INFO).getId();
        int shelterId = shelterDao.list().get(3).getId();

        // Make the initial claim, since user has non already, this should not be an issue
        dao.claimBeds(shelterDao, userId, shelterId, 1);

        // This call should throw the exception because the user already has a claim
        dao.claimBeds(shelterDao, userId, shelterId, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void claimBeds_shouldThrowWhenBedsIsNotPositive() {
        dao.claimBeds(shelterDao, dao.register(REG_INFO).getId(), shelterDao.list().get(0).getId(), 0);
    }
}
