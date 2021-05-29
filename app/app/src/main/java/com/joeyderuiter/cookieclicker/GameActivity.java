package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String ARGS_IP = "com.joeyderuiter.cookieclicker.ARGS_IP";

    private String address;
    private WebSocketClient webSocket;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        address = intent.getStringExtra(ARGS_IP);

        this.setupServices();
        this.setupWebsocketClient();
    }

    private void setupServices() {
        this.authService = AuthServiceLocator.getInstance(this);
    }

    private void setupWebsocketClient() {

        String userId = authService.getCurrentUserId();
        String uri = String.format("ws://%s/?id=%s", this.address,  userId);

        System.out.println("uri = " + uri);

        try {
            this.webSocket = new WebSocketClient(new URI(uri)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(String message) {
                    System.out.println("message = " + message);

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
}