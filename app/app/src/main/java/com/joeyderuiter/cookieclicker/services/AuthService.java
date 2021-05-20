package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
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

    public Task<AuthResult> loginByEmail(@NonNull String email, @NonNull String password) {
        return this.auth.signInWithEmailAndPassword(email, password);
    }
}
