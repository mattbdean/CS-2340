package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.BedClaim;
import com.github.alphabet26.model.Shelter;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Stores all Users in memory. Stores passwords as strings hashed with SHA-256 and no salt. Mostly
 * for testing purposes.
 */
public final class InMemoryUserDao extends InMemoryDao<UUID, User> implements UserDao {
    private final Object lock = new Object();

    public InMemoryUserDao() {
        super(new ArrayList<User>());
    }

    @Override
    public User register(UserRegistrationInfo info) {
        // Runs in linear time but that's fine for now
        synchronized (lock) {
            for (User u : data) {
                if (u.getEmail().equals(info.getEmail())) {
                    throw new IllegalArgumentException("Email already in use: " + u.getEmail());
                }
            }

            String hashed = naiveHash(info.getPlaintextPassword());
            User newUser = User.create(info, hashed);
            data.add(newUser);

            return newUser;
        }
    }

    @Override
    public User login(String email, String password) {
        String hash = naiveHash(password);

        synchronized (lock) {
            for (User u : data) {
                if (u.getEmail().equals(email) && u.getPasswordHash().equals(hash)) {
                    return u;
                }
            }
        }

        return null;
    }

    @Override
    public BedClaim claimBeds(ShelterDao shelterDao, UUID userId, int shelterId, int beds) {
        // Do non-resource intensive checks first
        if (beds <= 0)
            throw new IllegalArgumentException("Must request at least one bed");

        Shelter shelter = shelterDao.find(shelterId);
        // Make sure the shelter ID is valid
        if (shelter == null)
            throw new IllegalArgumentException("No known Shelter with ID " + shelterId);

        // Make sure we have enough beds
        if (shelter.getAvailableBeds() < beds)
            throw new IllegalArgumentException("Not enough beds at Shelter with ID " + shelterId);

        // Make sure the user ID is valid
        User user = find(userId);
        if (user == null)
            throw new IllegalArgumentException("No known User with ID " + userId);

        // ...and that it doesn't have a current bed claim
        if (user.getCurrentClaim() != null)
            throw new IllegalArgumentException("This user already has a BedClaim with shelter " +
                user.getCurrentClaim().getShelterId());

        // Update the user and shelter with the new claim
        BedClaim claim = BedClaim.create(shelterId, beds);
        update(user.withClaim(claim));
        shelterDao.update(shelter.withAvailableBeds(shelter.getAvailableBeds() - beds));

        return claim;
    }

    @Override
    public void releaseClaim(ShelterDao shelterDao, UUID userId) {
        User user = find(userId);

        if (user == null)
            throw new IllegalArgumentException("No user for UUID " + userId);

        if (user.getCurrentClaim() == null)
            // nothing to do
            return;

        Shelter shelter = shelterDao.find(user.getCurrentClaim().getShelterId());
        if (shelter == null)
            throw new IllegalArgumentException("BedClaim listed invalid shelter ID: " + user.getCurrentClaim().getShelterId());

        updateUser(user.withClaim(null));
        shelterDao.update(shelter.withAvailableBeds(shelter.getAvailableBeds() + user.getCurrentClaim().getBedCount()));
    }

    @Nullable private User updateUser(User newUser) {
        User previousInfo = null;

        for (int i = 0; i < data.size(); i++) {
            if (newUser.getId().equals(data.get(i).getId())) {
                previousInfo = data.set(i, newUser);
                break;
            }
        }

        return previousInfo;
    }

    /**
     * Hashes the given string using SHA-256. No salt is used, which is why this is a "naive" hash.
     */
    private String naiveHash(String str) {
        final String charset = "UTF-8";
        final String hashAlgo = "SHA-256";
        try {
            byte[] result = MessageDigest.getInstance(hashAlgo).digest(str.getBytes());
            return new String(result, charset);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Expected " + hashAlgo + " to be supported");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Expected charset " + charset + " to be available");
        }
    }

    @Override
    @Nullable
    public User find(UUID id) {
        synchronized (lock) {
            for (User u : data) {
                if (u.getId().equals(id)) {
                    return u;
                }
            }
        }

        return null;
    }
}
