package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.joeyderuiter.cookieclicker.fragments.authentication.LoginFragment;
import com.joeyderuiter.cookieclicker.fragments.authentication.RegisterFragment;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;

public class MainActivity extends AppCompatActivity {

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        this.setupServices();

        System.out.println("authService = " + authService.isAuthenticated());
    }

    private void setupServices() {
        authService = AuthServiceLocator.getInstance(this);
    }

    public void showLogin() {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.authentication_fragment, loginFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showRegister() {
        Fragment registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.authentication_fragment, registerFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

}
