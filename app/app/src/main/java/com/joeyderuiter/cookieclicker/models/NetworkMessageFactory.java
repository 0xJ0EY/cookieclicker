package com.joeyderuiter.cookieclicker.models;

import com.joeyderuiter.cookieclicker.models.user.Profile;

public class NetworkMessageFactory {
    public static Class<?> fromString(String name) {
        switch (name) {
            case "Profile":
                return Profile.class;
            default:
                return Void.class;
        }
    }
}
