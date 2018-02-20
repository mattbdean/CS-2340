package com.github.alphabet26;

/**
 * Created by vinda on 2/20/2018.
 */

public class User {
    private String name;
    private String username;
    private String password;
    private UserType userType;

    public User(String name, String username, String password, UserType userType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername()  { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

}
