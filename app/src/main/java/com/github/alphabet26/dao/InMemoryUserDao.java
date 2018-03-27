package com.github.alphabet26.dao;

import android.support.annotation.Nullable;

import com.github.alphabet26.model.BedClaim;
import com.github.alphabet26.model.Shelter;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Stores all Users in memory. Stores passwords as strings hashed with SHA-256 and no salt. Mostly
 * for testing purposes.
 */
public final class InMemoryUserDao implements UserDao {
    private final Object lock = new Object();
    private List<User> users;

    public InMemoryUserDao() {
        this.users = new LinkedList<>();
    }

    @Override
    public User register(UserRegistrationInfo info) {
        // Runs in linear time but that's fine for now
        synchronized (lock) {
            for (User u : users) {
                if (u.getUsername().equals(info.getUsername())) {
                    throw new IllegalArgumentException("Username already registered: " + u.getUsername());
                }
            }

            String hashed = naiveHash(info.getPlaintextPassword());
            User newUser = User.create(info, hashed);
            users.add(newUser);

            return newUser;
        }
    }

    @Override
    public User login(String username, String password) {
        String hash = naiveHash(password);

        synchronized (lock) {
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPasswordHash().equals(hash)) {
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

        Shelter shelter = shelterDao.pluck(shelterId);
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
        updateUser(user.withClaim(claim));
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

        Shelter shelter = shelterDao.pluck(user.getCurrentClaim().getShelterId());
        if (shelter == null)
            throw new IllegalArgumentException("BedClaim listed invalid shelter ID: " + user.getCurrentClaim().getShelterId());

        updateUser(user.withClaim(null));
        shelterDao.update(shelter.withAvailableBeds(shelter.getAvailableBeds() + user.getCurrentClaim().getBedCount()));
    }

    @Nullable private User updateUser(User newUser) {
        User previousInfo = null;

        for (int i = 0; i < users.size(); i++) {
            if (newUser.getId().equals(users.get(i).getId())) {
                previousInfo = users.set(i, newUser);
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
            for (User u : users) {
                if (u.getId().equals(id)) {
                    return u;
                }
            }
        }

        return null;
    }
}
