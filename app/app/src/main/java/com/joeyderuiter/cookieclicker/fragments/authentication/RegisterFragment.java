package com.joeyderuiter.cookieclicker.fragments.authentication;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.joeyderuiter.cookieclicker.MainActivity;
import com.joeyderuiter.cookieclicker.R;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button switchButton = (Button) view.findViewById(R.id.switch_to_login);
        switchButton.setOnClickListener(x -> switchToRegisterFragment());

        return view;
    }

    private void switchToRegisterFragment() {
        ((MainActivity) requireActivity()).showLogin();
    }

}