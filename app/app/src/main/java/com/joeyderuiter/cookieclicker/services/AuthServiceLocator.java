package com.joeyderuiter.cookieclicker.services;

import androidx.annotation.NonNull;
import android.content.Context;

public class AuthServiceLocator {
    public static AuthService instance;

    private AuthServiceLocator() {}

    public synchronized static AuthService getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new AuthService(context);
        }

        return instance;
    }

}
