package com.github.alphabet26;

import android.app.Application;
import android.support.annotation.NonNull;

public class App extends Application {
    private static App instance;

    private UserDao userDao;
    private User activeUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            this.userDao = new InMemoryUserDao();
            this.userDao.register(new UserRegistrationInfo("<testing user>", "username", "password", UserType.USER));
        } else {
            // TODO replace with other implementation such as Firebase or Android's SQLite db
            this.userDao = new InMemoryUserDao();
        }
    }

    @NonNull public UserDao getUserDao() { return userDao; }

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
}
