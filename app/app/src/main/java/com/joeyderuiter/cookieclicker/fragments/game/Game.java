package com.joeyderuiter.cookieclicker.fragments.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.fragments.game.tabs.ChartTab;
import com.joeyderuiter.cookieclicker.models.game.TabItem;
import com.joeyderuiter.cookieclicker.fragments.game.tabs.ScoreTab;
import com.joeyderuiter.cookieclicker.fragments.game.tabs.StoreTab;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.List;

public class Game extends Fragment {

    private GameViewModel gameViewModel;
    private final List<TabItem> tabItems;
    private TabViewAdapter tabViewAdapter;

    public Game() {
        this.tabItems = this.setupTabItems();
    }

    private List<TabItem> setupTabItems() {
        List<TabItem> items = new ArrayList<>();

        items.add(new TabItem("Store", new StoreTab()));
        items.add(new TabItem("Score", new ScoreTab()));
        items.add(new TabItem("Chart", new ChartTab()));

        return items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_game, container, false);
    }

    private void setupViewPager(View view) {

        tabViewAdapter = new TabViewAdapter(this, this.tabItems);

        // View pager itself
        ViewPager2 viewPager = view.findViewById(R.id.game_view_pager);
        viewPager.setAdapter(tabViewAdapter);

        // Register a manager for the tabs
        TabLayout tabLayout = view.findViewById(R.id.game_view_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            TabItem tabItem = tabItems.get(position);
            tab.setText(tabItem.getName());
        }).attach();
    }

    private void setupViewModel() {
        this.gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    private void setupProgressBar(View view) {
        ProgressBar progressBar = view.findViewById(R.id.game_view_time_left);

        this.gameViewModel.getServerTime().observe(getViewLifecycleOwner(), serverTime -> {
            progressBar.setMax(serverTime.getStartTime());
            progressBar.setProgress(serverTime.getTimeLeft());
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
        this.setupViewPager(view);
        this.setupProgressBar(view);

        tabViewAdapter = new TabViewAdapter(this, this.tabItems);

        ViewPager2 viewPager = view.findViewById(R.id.game_view_pager);
        viewPager.setAdapter(tabViewAdapter);

        TabLayout tabLayout = view.findViewById(R.id.game_view_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            TabItem tabItem = tabItems.get(position);

            if (tabItem == null)
                tabItem = tabItems.get(0);

            tab.setText(tabItem.getName());
        }).attach();
    }

    private static class TabViewAdapter extends FragmentStateAdapter {
        private final List<TabItem> items;

        public TabViewAdapter(Fragment fragment, List<TabItem> items) {
            super(fragment);
            this.items = items;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            TabItem tabItem = items.get(position);
            // We create a clone of the fragment. So we don't re-use them by accident
            return tabItem.getFragmentClone();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
