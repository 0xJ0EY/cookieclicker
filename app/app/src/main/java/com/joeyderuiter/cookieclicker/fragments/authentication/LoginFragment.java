package com.joeyderuiter.cookieclicker.fragments.authentication;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.joeyderuiter.cookieclicker.MainActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.services.AuthService;

public class LoginFragment extends Fragment {

    private AuthService authService;

    private TextInputLayout emailFieldLayout;
    private EditText emailField;

    private TextInputLayout passwordFieldLayout;
    private EditText passwordField;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.setupViewElements(view);

        this.authService = ((MainActivity) requireActivity()).getAuthService();

        return view;
    }

    private void setupViewElements(View view) {
        Button loginButton = view.findViewById(R.id.authentication_login_login_btn);
        loginButton.setOnClickListener(x -> this.onClickLogin());

        Button registerButton = view.findViewById(R.id.authentication_login_register_btn);
        registerButton.setOnClickListener(x -> this.onClickRegisterButton());

        emailFieldLayout = view.findViewById(R.id.authentication_login_email_layout);
        emailField = view.findViewById(R.id.authentication_login_email);

        passwordFieldLayout = view.findViewById(R.id.authentication_login_password_layout);
        passwordField = view.findViewById(R.id.authentication_login_password);
    }

    private void onClickLogin() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        boolean hasError = false;

        if (!validateEmail(email))
            hasError = true;

        if (!validatePassword(password))
            hasError = true;

        // Don't send the Firebase request if we have an error
        if (hasError) return;

        authService
            .loginByEmail(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ((MainActivity) requireActivity()).switchToLobbyActivity();
                } else {
                    showAccountNotFoundWarning();
                }
            });
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

    private void showAccountNotFoundWarning() {
        passwordFieldLayout.setErrorEnabled(true);
        passwordFieldLayout.setError("Account not found");
    }

    private void onClickRegisterButton() {
        ((MainActivity) requireActivity()).showRegister();
    }
}