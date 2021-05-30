package com.joeyderuiter.cookieclicker.fragments.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.adapters.GameLobbyListAdapter;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.jetbrains.annotations.NotNull;

public class GameLobby extends Fragment {

    private GameViewModel gameViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        this.gameViewModel.getPlayers().observe(getViewLifecycleOwner(), playerList -> {
            System.out.println("playerList.getPlayers().size() = " + playerList.getPlayers().size());
        });
    }

    private void setupLobbyListView(View view) {
        RecyclerView lobbyListView = (RecyclerView) view.findViewById(R.id.lobbyList);
        GameLobbyListAdapter adapter = new GameLobbyListAdapter(getViewLifecycleOwner(), this.gameViewModel);

        lobbyListView.setAdapter(adapter);
        lobbyListView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_lobby, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
        this.setupLobbyListView(view);
    }
}