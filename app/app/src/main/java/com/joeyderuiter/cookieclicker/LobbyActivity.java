package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.joeyderuiter.cookieclicker.adapters.LobbyListAdapter;
import com.joeyderuiter.cookieclicker.services.LobbyService;
import com.joeyderuiter.cookieclicker.services.LobbyServiceLocator;

public class LobbyActivity extends AppCompatActivity {

    private LobbyService lobbyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        this.setupServices();
        this.setupLobbyListView();
    }

    private void setupServices() {
        this.lobbyService = LobbyServiceLocator.getInstance(this);
    }

    private void setupLobbyListView() {
        RecyclerView lobbyListView = (RecyclerView) findViewById(R.id.score_list);
        LobbyListAdapter adapter = new LobbyListAdapter(this, this.lobbyService);

        lobbyListView.setAdapter(adapter);
        lobbyListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void joinLobby(String ip) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.ARGS_IP, ip);
        startActivity(intent);
    }
}