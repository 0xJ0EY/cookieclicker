package com.joeyderuiter.cookieclicker.models.game.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.CloneableFragment;

public class StoreTab extends CloneableFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_tab_store, container, false);
    }
}
