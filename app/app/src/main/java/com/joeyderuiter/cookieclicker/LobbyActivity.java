package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.joeyderuiter.cookieclicker.adapters.LobbyListAdapter;
import com.joeyderuiter.cookieclicker.models.user.Profile;
import com.joeyderuiter.cookieclicker.services.LobbyService;
import com.joeyderuiter.cookieclicker.services.LobbyServiceLocator;
import com.joeyderuiter.cookieclicker.services.NetworkMessageService;

public class LobbyActivity extends AppCompatActivity {

    private LobbyService lobbyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        this.setupServices();
        this.setupLobbyListView();

        String encodedMessage = NetworkMessageService.encodeMessage(new Profile("Joey"));
        System.out.println("encodedMessage = " + encodedMessage);

        String decodedMessage = NetworkMessageService.decodeMessage(encodedMessage);
        Class<?> networkMessage = NetworkMessageService.getMessageType(decodedMessage);

        if (networkMessage == Profile.class) {
            Profile profile = (Profile) NetworkMessageService.getMessageData(decodedMessage, networkMessage);

            System.out.println("username = " + profile.getUsername());
        }

        System.out.println("networkMessage = " + networkMessage);
    }

    private void setupServices() {
        this.lobbyService = LobbyServiceLocator.getInstance(this);
    }

    private void setupLobbyListView() {
        RecyclerView lobbyListView = (RecyclerView) findViewById(R.id.lobbyList);
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