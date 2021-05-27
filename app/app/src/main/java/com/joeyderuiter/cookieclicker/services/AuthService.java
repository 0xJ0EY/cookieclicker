package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.joeyderuiter.cookieclicker.models.user.Profile;

public class AuthService {

    final static String DATABASE_TABLE = "profiles";

    private final FirebaseAuth auth;
    private final DatabaseReference database;

    public AuthService(@NonNull Context context) {
        FirebaseApp.initializeApp(context);
        this.auth = FirebaseAuth.getInstance();

        this.database = FirebaseDatabase
                .getInstance()
                .getReference(DATABASE_TABLE);
    }

    public boolean isAuthenticated() {
        return this.auth.getCurrentUser() != null;
    }

    public Task<AuthResult> loginByEmail(@NonNull String email, @NonNull String password) {
        return this.auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(@NonNull String email, @NonNull String password) {
        return this.auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> createUserProfile(@NonNull String uid, @NonNull Profile profile) {
        return this.database.child(uid).setValue(profile);
    }

}
