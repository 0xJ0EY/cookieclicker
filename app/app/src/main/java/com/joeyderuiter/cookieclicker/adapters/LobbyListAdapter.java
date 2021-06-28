package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joeyderuiter.cookieclicker.LobbyActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.lobby.Server;
import com.joeyderuiter.cookieclicker.services.LobbyService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.ViewHolder> {
    private static final String TAG = "LobbyListAdapter";
    private final ArrayList<Server> serverList;
    private final LobbyActivity lobbyActivity;

    private final TextView emptySeverMessage;

    public LobbyListAdapter(LobbyActivity lobbyActivity, LobbyService lobbyService, TextView emptySeverMessage) {
        this.lobbyActivity = lobbyActivity;
        this.emptySeverMessage = emptySeverMessage;
        this.serverList = new ArrayList<>();

        lobbyService.getLobbyList().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serverList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Server server = data.getValue(Server.class);

                    serverList.add(server);
                }

                notifyDataSetChanged();

                emptySeverMessage.setVisibility(serverList.size() > 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Firebase read failed");
            }
        });
    }

    @Override
    @NotNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View lobbyListItemView = inflater.inflate(R.layout.component_lobby_list_item, parent, false);

        return new ViewHolder(lobbyListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyListAdapter.ViewHolder holder, int position) {
        Server server = this.serverList.get(position);

        TextView textView = holder.serverName;
        textView.setText(server.ip);
        
        holder.joinLobbyBtn.setOnClickListener(event -> {
            lobbyActivity.joinLobby(server);
        });
    }

    @Override
    public int getItemCount() {
        return this.serverList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView serverName;
        public Button joinLobbyBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            serverName = (TextView) itemView.findViewById(R.id.serverName);
            joinLobbyBtn = (Button) itemView.findViewById(R.id.joinLobbyBtn);
        }
    }
}
