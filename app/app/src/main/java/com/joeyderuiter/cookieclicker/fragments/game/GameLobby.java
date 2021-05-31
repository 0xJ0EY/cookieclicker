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
import android.widget.Button;

import com.joeyderuiter.cookieclicker.GameActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.adapters.GameLobbyListAdapter;
import com.joeyderuiter.cookieclicker.models.messages.LobbyVote;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GameLobby extends Fragment {

    private GameViewModel gameViewModel;
    private AuthService authService;

    private Button startGameBtn;
    private Button toggleReadyBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.authService = AuthServiceLocator.getInstance(requireContext());
    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    private void setupLobbyListView(View view) {
        RecyclerView lobbyListView = (RecyclerView) view.findViewById(R.id.lobbyList);

        GameLobbyListAdapter adapter = new GameLobbyListAdapter(
                getViewLifecycleOwner(),
                this.gameViewModel
        );

        lobbyListView.setAdapter(adapter);
        lobbyListView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupButtons(View view) {
        this.setupReadyBtn(view);
        this.setupShowGameBtn(view);
    }

    private void setupReadyBtn(View view) {
        toggleReadyBtn = (Button) view.findViewById(R.id.toggleReadyBtn);

        toggleReadyBtn.setOnClickListener(event -> {
            toggleReadyBtn.setEnabled(false);

            ((GameActivity) requireActivity()).sendWsMessage(new LobbyVote());
        });
    }

    private void setupShowGameBtn(View view) {
        startGameBtn = (Button) view.findViewById(R.id.startGameBtn);

        // Show the start game button only if the player is the leader.
        this.gameViewModel.getPlayers().observe(getViewLifecycleOwner(), playerList -> {
            boolean showGameButton = false;

            for (Player player : playerList.getPlayers()) {
                if (player.isLeader() && this.authService.isCurrentPlayer(player)) {
                    showGameButton = true;
                }
            }

            startGameBtn.setVisibility(showGameButton ? View.VISIBLE : View.GONE);
        });
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
        this.setupButtons(view);
    }
}