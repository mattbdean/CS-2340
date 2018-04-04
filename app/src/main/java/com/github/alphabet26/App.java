package com.github.alphabet26;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.alphabet26.dao.InMemoryShelterDao;
import com.github.alphabet26.dao.InMemoryUserDao;
import com.github.alphabet26.dao.ShelterDao;
import com.github.alphabet26.dao.UserDao;
import com.github.alphabet26.model.ModelAdapterFactory;
import com.github.alphabet26.model.Shelter;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;
import com.github.alphabet26.model.UserType;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okio.Okio;

/**
 * Main application
 */
public class App extends Application {
    private static App instance;

    private UserDao userDao;
    private ShelterDao shelterDao;


    private UUID activeUser;

    @Override
    public void onCreate() {
        Moshi moshi;
        JsonAdapter<List<Shelter>> shelterAdapter;
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            this.userDao = new InMemoryUserDao();
            this.userDao.register(UserRegistrationInfo.create(
                "<testing user>", "username", "password", UserType.USER));
        } else {
            // TODO replace with other implementation such as Firebase or Android's SQLite db
            this.userDao = new InMemoryUserDao();
        }

        moshi = new Moshi.Builder()
            .add(ModelAdapterFactory.create())
            .build();
        shelterAdapter = moshi.adapter(Types.newParameterizedType(List.class, Shelter.class));

        List<Shelter> shelters = null;
        try {
            shelters = shelterAdapter.fromJson(Okio.buffer(Okio.source(getAssets().open(
                "shelters.json"))));
        } catch (IOException e) {
            Log.e(App.class.getSimpleName(), "Unable to load shelters from the CSV");
        }

        if (shelters == null) {
            shelters = new ArrayList<>();
        }
        this.shelterDao = new InMemoryShelterDao(shelters);
    }

    /**
     * Getter method for user dao
     * @return the user dao
     */
    @NonNull public UserDao getUserDao() { return userDao; }

    /**
     * Getter method for shelter dao
     * @return shelter dao
     */
    @NonNull public ShelterDao getShelterDao() { return shelterDao; }

    /**
     * Getter method for the active user's id
     * @return the active user's id
     */
    public UUID getActiveUserId() {
        if (activeUser == null) {
            throw new IllegalStateException("No active user");
        }

        return activeUser;
    }

    /** Updates the active user to the one provided
     * @param user is the user*/
    public void onLogin(@NonNull User user) { this.activeUser = user.getId(); }

    /** Removes the reference to the active user */
    public void onLogout() { this.activeUser = null; }

    /**
     * Gets this class: App
     * @return this class
     */
    @NonNull public static App get() { return instance; }
}
