package com.joeyderuiter.cookieclicker.fragments.authentication;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.joeyderuiter.cookieclicker.MainActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.user.Profile;
import com.joeyderuiter.cookieclicker.services.AuthService;

public class RegisterFragment extends Fragment {

    AuthService authService;

    private TextInputLayout usernameFieldLayout;
    private EditText usernameField;

    private TextInputLayout emailFieldLayout;
    private EditText emailField;

    private TextInputLayout passwordFieldLayout;
    private EditText passwordField;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        this.setupViewElements(view);

        this.authService = ((MainActivity) getActivity()).getAuthService();

        return view;
    }

    private void setupViewElements(View view) {
        Button loginButton = view.findViewById(R.id.authentication_register_login_btn);
        loginButton.setOnClickListener(x -> this.onClickLogin());

        Button registerButton = view.findViewById(R.id.authentication_register_register_btn);
        registerButton.setOnClickListener(x -> this.onClickRegisterButton());

        usernameFieldLayout = view.findViewById(R.id.authentication_register_username_layout);
        usernameField = view.findViewById(R.id.authentication_register_username);

        emailFieldLayout = view.findViewById(R.id.authentication_register_email_layout);
        emailField = view.findViewById(R.id.authentication_register_email);

        passwordFieldLayout = view.findViewById(R.id.authentication_register_password_layout);
        passwordField = view.findViewById(R.id.authentication_register_password);
    }

    private void onClickLogin() {
        ((MainActivity) requireActivity()).showLogin();
    }

    private void onClickRegisterButton() {
        String username = usernameField.getText().toString();
        String email    = emailField.getText().toString();
        String password = passwordField.getText().toString();

        boolean hasError = false;

        if (!validateUsername(username))
            hasError = true;

        if (!validateEmail(email))
            hasError = true;

        if (!validatePassword(password))
            hasError = true;

        // Don't send the Firebase request if we have an error
        if (hasError) return;

        this.registerUser(username, email, password);
    }

    private void registerUser(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.authService.register(email, password).addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                String uid = result.getResult().getUser().getUid();
                Profile profile = new Profile(username);

                this.createUserProfile(uid, profile);
            }
        });
    }

    private void createUserProfile(@NonNull String uid, @NonNull Profile profile) {

        Task<Void> test = this.authService.createUserProfile(uid, profile);

        test.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                ((MainActivity) requireActivity()).switchToLobbyActivity();
            }
        });
    }


    private boolean validateUsername(String username) {
        if (username.length() == 0) {
            usernameFieldLayout.setErrorEnabled(true);
            usernameFieldLayout.setError("Please enter your password");

            return false;
        }

        usernameFieldLayout.setErrorEnabled(false);
        usernameFieldLayout.setError(null);

        return true;
    }

    private boolean validateEmail(String username) {
        if (username.length() == 0) {
            emailFieldLayout.setErrorEnabled(true);
            emailFieldLayout.setError("Please enter your email");

            return false;
        }

        emailFieldLayout.setErrorEnabled(false);
        emailFieldLayout.setError(null);

        return true;
    }

    private boolean validatePassword(String password) {
        if (password.length() == 0) {
            passwordFieldLayout.setErrorEnabled(true);
            passwordFieldLayout.setError("Please enter your password");

            return false;
        }

        passwordFieldLayout.setErrorEnabled(false);
        passwordFieldLayout.setError(null);

        return true;
    }

}