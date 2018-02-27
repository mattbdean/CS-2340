package com.github.alphabet26;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static App instance;

    private UserDao userDao;
    private ShelterDao shelterDao;

    private User activeUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            this.userDao = new InMemoryUserDao();
            this.userDao.register(new UserRegistrationInfo("<testing user>", "username", "password", UserType.USER));

            List<Shelter> tempShelters = new ArrayList<>();
            tempShelters.add(new Shelter("Test", 5, 2, 100, 100, "addy", "test", Gender.MALE));
            tempShelters.add(new Shelter("Test2", 10, 5, 100, 100, "addy", "test", Gender.FEMALE));

            this.shelterDao = new InMemoryShelterDao(tempShelters);
        } else {
            // TODO replace with other implementation such as Firebase or Android's SQLite db
            this.userDao = new InMemoryUserDao();
            this.shelterDao = new InMemoryShelterDao();
        }
    }

    @NonNull public UserDao getUserDao() { return userDao; }
    @NonNull public ShelterDao getShelterDao() { return shelterDao; }

    public User getActiveUser() {
        if (activeUser == null) {
            throw new IllegalStateException("No active user");
        }

        return activeUser;
    }

    /** Returns true if and only if there is an active user */
    public boolean hasActiveUser() {
        return activeUser != null;
    }

    /** Updates the active user to the one provided */
    public void onLogin(@NonNull User user) { this.activeUser = user; }

    /** Removes the reference to the active user */
    public void onLogout() { this.activeUser = null; }

    @NonNull public static App get() { return instance; }

    private static List<Shelter> loadFromCsv(String filename) {
        // TODO
        return new ArrayList<>();
    }
}
