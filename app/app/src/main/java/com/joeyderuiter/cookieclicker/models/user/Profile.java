package com.joeyderuiter.cookieclicker.models.user;

public class Profile {

    private String username;

    // Empty constructor required for Jackson deserialization
    public Profile() {}

    public Profile(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
