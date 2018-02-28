package com.github.alphabet26;

import android.support.annotation.Nullable;

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
