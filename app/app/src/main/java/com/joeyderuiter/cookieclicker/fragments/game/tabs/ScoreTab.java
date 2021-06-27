package com.joeyderuiter.cookieclicker.fragments.game.tabs;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.adapters.GameScoreListAdapter;
import com.joeyderuiter.cookieclicker.models.game.CloneableFragment;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.jetbrains.annotations.NotNull;

public class ScoreTab extends CloneableFragment {

    private GameViewModel gameViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_tab_score, container, false);
    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    private void setupScoreListView(View view) {
        RecyclerView lobbyListView = (RecyclerView) view.findViewById(R.id.score_list);

        GameScoreListAdapter adapter = new GameScoreListAdapter(
                requireContext(),
                getViewLifecycleOwner(),
                this.gameViewModel
        );

        lobbyListView.setAdapter(adapter);
        lobbyListView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
        this.setupScoreListView(view);
    }
}
