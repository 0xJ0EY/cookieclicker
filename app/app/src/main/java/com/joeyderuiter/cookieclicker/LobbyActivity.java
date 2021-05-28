package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        RecyclerView lobbyListView = (RecyclerView) findViewById(R.id.lobbyList);
        LobbyListAdapter adapter = new LobbyListAdapter(this.lobbyService);

        lobbyListView.setAdapter(adapter);
        lobbyListView.setLayoutManager(new LinearLayoutManager(this));
    }
}