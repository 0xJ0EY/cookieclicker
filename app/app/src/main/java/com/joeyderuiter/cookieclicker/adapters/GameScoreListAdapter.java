package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.NumberFormatFactory;
import com.joeyderuiter.cookieclicker.models.game.Powerup;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.models.user.PlayerPowerup;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

import lombok.Getter;

public class GameScoreListAdapter extends RecyclerView.Adapter<GameScoreListAdapter.ViewHolder> {

    private Context context;
    private PlayerList playerList;

    public GameScoreListAdapter(Context context, LifecycleOwner lifecycleOwner, GameViewModel gameViewModel) {
        this.context = context;

        gameViewModel.getPlayers().observe(lifecycleOwner, playerList -> {
            playerList.getPlayers().sort((x, y)-> (int) (y.getTotalCookies() - x.getTotalCookies()));
            this.playerList = playerList;

            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public GameScoreListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View gameScoreListItemView = inflater.inflate(R.layout.component_game_score_list_item, parent, false);

        return new GameScoreListAdapter.ViewHolder(gameScoreListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GameScoreListAdapter.ViewHolder holder, int position) {
        Player player = playerList.getPlayers().get(position);

        long totalCookies = player.getTotalCookies();

        holder.getPlayerName().setText(player.getUsername());

        NumberFormat nf = NumberFormatFactory.getInstance();
        String scoreFormat = nf.format(totalCookies);

        String cookieText = totalCookies == 1 ?
                scoreFormat + " cookie" :
                scoreFormat + " cookies";

        holder.getPlayerTotalCookies().setText(cookieText);

        renderPlayerPowerups(holder, player);

    }

    private void renderPlayerPowerups(GameScoreListAdapter.ViewHolder holder, Player player) {
        LinearLayout layout = holder.getPlayerPowerupContainer();
        layout.removeAllViewsInLayout();

        for (PlayerPowerup playerPowerup : player.getPowerups()) {
            TextView textView = new TextView(context);

            NumberFormat nf = NumberFormatFactory.getInstance();
            String amountFormat = nf.format(playerPowerup.getAmount());

            String powerupText = amountFormat + " " + playerPowerup.getPowerup().getName();

            textView.setWidth(layout.getWidth());

            textView.setText(powerupText);
            layout.addView(textView);
        }
    }

    @Override
    public int getItemCount() {
        if (this.playerList == null) return 0;

        return this.playerList
                .getPlayers()
                .size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Getter
        private final TextView playerName;

        @Getter
        private final TextView playerTotalCookies;

        @Getter
        private final LinearLayout playerPowerupContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            playerName          = (TextView) itemView.findViewById(R.id.player_name);
            playerTotalCookies  = (TextView) itemView.findViewById(R.id.player_total_cookies);
            playerPowerupContainer = (LinearLayout) itemView.findViewById(R.id.player_powerup_container);
        }
    }

}
