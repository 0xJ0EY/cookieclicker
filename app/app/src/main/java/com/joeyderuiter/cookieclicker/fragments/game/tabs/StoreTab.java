package com.joeyderuiter.cookieclicker.fragments.game.tabs;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.GameActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.adapters.PowerupStoreListAdapter;
import com.joeyderuiter.cookieclicker.components.CookieButton;
import com.joeyderuiter.cookieclicker.models.NumberFormatFactory;
import com.joeyderuiter.cookieclicker.models.game.CloneableFragment;
import com.joeyderuiter.cookieclicker.models.game.ShopPowerup;
import com.joeyderuiter.cookieclicker.models.messages.CookieClick;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;
import com.joeyderuiter.cookieclicker.services.StoreService;
import com.joeyderuiter.cookieclicker.services.StoreServiceLocator;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StoreTab extends CloneableFragment {

    private GameViewModel gameViewModel;
    private AuthService authService;
    private StoreService storeService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.authService = AuthServiceLocator.getInstance(requireContext());
        this.storeService = StoreServiceLocator.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_tab_store, container, false);
    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    private void setupCookieButton(View view) {
        CookieButton cookieButton = view.findViewById(R.id.cookie_button);
        cookieButton.registerHandler(() -> {
            ((GameActivity) requireActivity()).sendWsMessage(new CookieClick(true));
        });
    }

    private void setupCookieCounter(View view) {
        TextView cookieCounter = view.findViewById(R.id.cookie_counter);

        gameViewModel.getPlayers().observe(getViewLifecycleOwner(), playerList -> {

            for (Player player : playerList.getPlayers()) {
                if (!authService.isCurrentPlayer(player)) continue;

                NumberFormat nf = NumberFormatFactory.getInstance();
                String scoreFormat = nf.format(player.getCookies());

                String cookieText = player.getCookies() == 1 ?
                        scoreFormat + " cookie" :
                        scoreFormat + " cookies";

                cookieCounter.setText(cookieText);
            }
        });
    }

    private void setupPowerupStore(View view) {
        RecyclerView storeListView = (RecyclerView) view.findViewById(R.id.powerup_store_list);

        PowerupStoreListAdapter adapter = new PowerupStoreListAdapter(
                (GameActivity) requireActivity(),
                this.storeService,
                this.authService,
                this.gameViewModel
        );

        storeListView.setAdapter(adapter);
        storeListView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
        this.setupCookieButton(view);
        this.setupCookieCounter(view);
        this.setupPowerupStore(view);
    }
}
