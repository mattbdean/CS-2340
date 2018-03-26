package com.github.alphabet26;

import android.app.Application;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.alphabet26.dao.InMemoryShelterDao;
import com.github.alphabet26.dao.InMemoryUserDao;
import com.github.alphabet26.dao.ShelterDao;
import com.github.alphabet26.dao.UserDao;
import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.Shelter;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;
import com.github.alphabet26.model.UserType;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        } else {
            // TODO replace with other implementation such as Firebase or Android's SQLite db
            this.userDao = new InMemoryUserDao();
        }

        List<Shelter> shelters;
        try {
            shelters = loadFromCsv();
        } catch (IOException e) {
            Log.e(App.class.getSimpleName(), "Unable to load shelters from the CSV");
            shelters = new ArrayList<>();
        }

        this.shelterDao = new InMemoryShelterDao(shelters);
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

    private List<Shelter> loadFromCsv() throws IOException {
        List<Shelter> shelters;
        InputStream in = null;
        try {
            in = getAssets().open("shelters.csv", AssetManager.ACCESS_BUFFER);
            shelters = parseCsv(in);
        } catch (IOException e) {
            Log.e(App.class.getSimpleName(), "Unable to load shelters from CSV", e);
            shelters = new ArrayList<>();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return shelters;
    }

    private static List<Shelter> parseCsv(InputStream in) {
        // Get a list of String arrays. Each element in the list is a row and each element in the
        // array is a cell.
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaders("Unique Key", "Shelter Name", "Capacity", "Restrictions", "Longitude", "Latitude", "Address", "Special Notes", "Phone Number");
        CsvParser parser = new CsvParser(settings);
        List<String[]> rows = parser.parseAll(new InputStreamReader(in));

        // Convert the parsed CSV values to Shelter objects
        List<Shelter> shelters = new ArrayList<>(rows.size());
        for (String[] row : rows.subList(1, rows.size())) {
            shelters.add(Shelter.create(
                    Integer.parseInt(row[0]),
                    row[1],
                    row[2] == null ? "" : row[2],
                    row[3] == null ? Gender.ANY : Gender.valueOf(row[3].toUpperCase()),
                    row[4] == null ? AgeRange.ANY : AgeRange.valueOf(row[4].toUpperCase()),
                    Float.parseFloat(row[5]),
                    Float.parseFloat(row[6]),
                    row[7],
                    row[9],
                    row[8],
                    Integer.parseInt(row[10])
            ));
        }

        // Try to clean up
        try {
            in.close();
        } catch (IOException e) {
            Log.e(App.class.getSimpleName(), "Unable to close CSV input stream", e);
        }

        return shelters;
    }
}
