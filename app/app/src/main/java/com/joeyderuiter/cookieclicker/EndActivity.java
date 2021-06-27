package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.joeyderuiter.cookieclicker.models.lobby.Server;
import com.joeyderuiter.cookieclicker.models.messages.EndData;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScore;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScores;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.services.EndService;
import com.joeyderuiter.cookieclicker.services.EndServiceLocator;
import com.joeyderuiter.cookieclicker.services.StoreServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndActivity extends AppCompatActivity {

    private EndService endService;
    private EndData endData;

    private static final int[] colours = {
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent intent = getIntent();
        Server server = (Server) intent.getSerializableExtra(GameActivity.ARGS_IP);

        endService = EndServiceLocator.getInstance(this);
        endService.configure(server);

        this.fetchEndData();
        this.setCurrentScore();
        this.drawPlayerGraph();
    }

    private void fetchEndData() {
        this.endData = this.endService.fetchEndData();
    }

    private void setCurrentScore() {
        int currentScore = this.endData.getPersonalScore();
        TextView textView = this.findViewById(R.id.end_current_score);
        textView.setText(String.valueOf(currentScore));
    }

    private void drawPlayerGraph() {
        LineChart chart = (LineChart) this.findViewById(R.id.end_progress_chart);

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
        {
            if (endData.getPlayerScores().size() <= 0) return;

            dataSets.clear();

            HashMap<String, List<Entry>> playerData = new HashMap<>();

            // Create the first entry of the hashmap, so we can have a branchless loop while processing
            // The data.
            endData.getPlayerScores().get(0).getScores().forEach(score -> {
                playerData.put(score.getPlayerId(), new ArrayList<>());
            });

            for (PlayerScores scores : endData.getPlayerScores()) {
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
        };

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }


    private String getPlayerName(String playerId) {
        for (Player player : this.endData.getPlayers()) {{
            if (player.getId().equals(playerId))
                return player.getUsername();
        }}

        return "";
    }


}