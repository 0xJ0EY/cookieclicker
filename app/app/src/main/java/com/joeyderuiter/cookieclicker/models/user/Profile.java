package com.joeyderuiter.cookieclicker.models.user;

public class Profile {

    private String username;

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
