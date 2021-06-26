package com.joeyderuiter.cookieclicker.fragments.game.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.CloneableFragment;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

public class ChartTab extends CloneableFragment {

    private GameViewModel gameViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_tab_chart, container, false);
    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
    }

}
