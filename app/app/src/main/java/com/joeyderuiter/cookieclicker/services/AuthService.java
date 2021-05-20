package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {

    private final FirebaseAuth auth;

    public AuthService(@NonNull Context context) {
        FirebaseApp.initializeApp(context);
        this.auth = FirebaseAuth.getInstance();
    }

    public boolean isAuthenticated() {
        return this.auth.getCurrentUser() != null;
    }
}
