package com.joeyderuiter.cookieclicker.fragments.authentication;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.joeyderuiter.cookieclicker.MainActivity;
import com.joeyderuiter.cookieclicker.R;

public class LoginFragment extends Fragment {

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

        return view;
    }

    private void setupViewElements(View view) {
        Button loginButton = view.findViewById(R.id.authentication_login_login_btn);
        loginButton.setOnClickListener(x -> this.onClickLogin());

        Button registerButton = view.findViewById(R.id.authentication_login_register_btn);
        registerButton.setOnClickListener(x -> this.onClickRegisterButton());
    }

    private void onClickLogin() {

    }

    private void onClickRegisterButton() {
        ((MainActivity) requireActivity()).showRegister();
    }
}