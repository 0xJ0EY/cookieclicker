package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import lombok.Getter;
import lombok.Setter;

public class GameLobbyListAdapter extends RecyclerView.Adapter<GameLobbyListAdapter.ViewHolder> {
    private static final String TAG = "GameLobbyListAdapter";

    private PlayerList playerList;

    public GameLobbyListAdapter(LifecycleOwner lifecycleOwner, GameViewModel gameViewModel) {
        gameViewModel.getPlayers().observe(lifecycleOwner, playerList -> {
            this.playerList = playerList;

            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public GameLobbyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View lobbyListItemView = inflater.inflate(R.layout.component_game_lobby_list_item, parent, false);

        return new GameLobbyListAdapter.ViewHolder(lobbyListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameLobbyListAdapter.ViewHolder holder, int position) {
        Player player = playerList.getPlayers().get(position);

        holder.getPlayerName().setText(player.getUsername());

        holder.getPlayerName().setTextColor(player.isReady() ?
                Color.rgb(0, 200 ,0) :
                Color.rgb(200, 0 ,0)
        );

        System.out.println("player.isLeader() = " + player.isLeader());

        if (player.isLeader())
            holder.getPlayerName().setPaintFlags(holder.getPlayerName().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
        @Setter
        private TextView playerName;

        public ViewHolder(View itemView) {
            super(itemView);

            playerName = (TextView) itemView.findViewById(R.id.playerName);
        }
    }
}
