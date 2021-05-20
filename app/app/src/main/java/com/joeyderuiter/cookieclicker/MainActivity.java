package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;

public class MainActivity extends AppCompatActivity {

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = AuthServiceLocator.getInstance(this);

        System.out.println("authService = " + authService.isAuthenticated());
    }

}
