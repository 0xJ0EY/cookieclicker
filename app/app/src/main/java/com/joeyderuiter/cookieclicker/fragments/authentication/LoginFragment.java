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

        Button switchButton = (Button) view.findViewById(R.id.switch_to_register);
        switchButton.setOnClickListener(x -> switchToRegisterFragment());

        return view;
    }

    private void switchToRegisterFragment() {
        ((MainActivity) requireActivity()).showRegister();
    }
}