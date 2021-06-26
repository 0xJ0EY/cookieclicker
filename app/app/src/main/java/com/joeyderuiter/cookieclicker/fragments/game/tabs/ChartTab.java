package com.joeyderuiter.cookieclicker.fragments.game.tabs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.CloneableFragment;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScore;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScores;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartTab extends CloneableFragment {

    private GameViewModel gameViewModel;
    private LineChart chart;


    private static final int[] colours = {
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80)
    };

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

    private void setupGraph(View view) {
        chart = (LineChart) view.findViewById(R.id.progress_chart);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);

        chart.getXAxis().setEnabled(false);
        chart.setTouchEnabled(false);

        List<ILineDataSet> dataSets = new ArrayList<>();
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();

        // It is not possible to move this to its own function, as this makes the chart crash?
        this.gameViewModel.getPlayerScore().observe(requireActivity(), container -> {
            if (container.getPlayerScores().size() <= 0) return;

            dataSets.clear();

            HashMap<String, List<Entry>> playerData = new HashMap<>();

            // Create the first entry of the hashmap, so we can have a branchless loop while processing
            // The data.
            container.getPlayerScores().get(0).getScores().forEach(score -> {
                playerData.put(score.getPlayerId(), new ArrayList<>());
            });

            for (PlayerScores scores : container.getPlayerScores()) {
                int time = scores.getTime();

                for (PlayerScore score : scores.getScores()) {
                    Entry value = new Entry((float) time, (float) score.getScore());

                    playerData.get(score.getPlayerId())
                            .add(value);
                }
            }

            int index = 0;

            for (Map.Entry<String, List<Entry>> entry : playerData.entrySet()) {
                String playerId = entry.getKey();
                List<Entry> values = entry.getValue();

                String playerName = getPlayerName(playerId);
                LineDataSet dataSet = new LineDataSet(values, playerName);

                dataSet.setColor(colours[index++]);
                dataSet.setDrawCircles(false);
                dataSet.setDrawValues(false);
                dataSet.setLineWidth(2.5f);

                dataSets.add(dataSet);
            }

            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    private String getPlayerName(String playerId) {

        PlayerList playerList = this.gameViewModel.getPlayers().getValue();
        if (playerList == null) return "";

        for (Player player : playerList.getPlayers()) {
            if (player.getId().equals(playerId))
                return player.getUsername();
        }

        return "";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setupViewModel();
        this.setupGraph(view);
    }

}
