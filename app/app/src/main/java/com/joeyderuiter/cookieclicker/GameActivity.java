package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;
import com.joeyderuiter.cookieclicker.services.NetworkMessageService;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Getter;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String ARGS_IP = "com.joeyderuiter.cookieclicker.ARGS_IP";

    private WebSocketClient webSocket;
    private AuthService authService;

    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        String address = intent.getStringExtra(ARGS_IP);

        this.setupServices();
        this.setupViewModel();
        this.setupWebSocketClient(address);
    }

    private void setupServices() {
        this.authService = AuthServiceLocator.getInstance(this);
    }

    private void setupViewModel() {
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
    }

    private void setupWebSocketClient(String address) {

        String userId = authService.getCurrentUserId();
        String uri = String.format("ws://%s/?id=%s", address,  userId);

        System.out.println("uri = " + uri);

        try {
            this.webSocket = new WebSocketClient(new URI(uri)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(String message) {
                    handleWsMessages(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {

                }
            };
        } catch (URISyntaxException ex) {
            System.out.println("ex = " + ex);
            Log.e(TAG, "Unable to parse the URI");
        }

        this.webSocket.connect();
    }

    public void sendWsMessage(Object message) {
        String encodedMessage = NetworkMessageService.encodeMessage(message);
        this.webSocket.send(encodedMessage);
    }

    private void handleWsMessages(String message) {
        String decodedMessage   = NetworkMessageService.decodeMessage(message);
        Class<?> messageType    = NetworkMessageService.getMessageType(decodedMessage);

        if (messageType == PlayerList.class) {
            PlayerList playerList = (PlayerList) NetworkMessageService.getMessageData(decodedMessage, messageType);

            gameViewModel.setPlayers(playerList);
        }
    }
}