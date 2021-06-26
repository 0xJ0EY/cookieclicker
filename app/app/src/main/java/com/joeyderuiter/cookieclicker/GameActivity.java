package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joeyderuiter.cookieclicker.fragments.game.Game;
import com.joeyderuiter.cookieclicker.models.lobby.Server;
import com.joeyderuiter.cookieclicker.models.messages.ChangeState;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.messages.ServerTime;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScoresContainer;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.AuthServiceLocator;
import com.joeyderuiter.cookieclicker.services.NetworkMessageService;
import com.joeyderuiter.cookieclicker.services.StoreServiceLocator;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String ARGS_IP = "com.joeyderuiter.cookieclicker.ARGS_IP";

    private WebSocketClient webSocket;
    private AuthService authService;

    private GameViewModel gameViewModel;

    private final Map<Class<?>, Consumer<String>> messageHandlers = this.setupMessageHandlers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        Server server = (Server) intent.getSerializableExtra(ARGS_IP);

        String address = server.ip + ":" + server.port;

        StoreServiceLocator
                .getInstance(this)
                .configure(server);

        this.setupServices();
        this.setupViewModel();
        this.setupWebSocketClient(address);
    }

    private Map<Class<?>, Consumer<String>> setupMessageHandlers() {
        Map<Class<?>, Consumer<String>> handlers = new HashMap<>();

        handlers.put(PlayerList.class, decodedMessage -> {
            PlayerList playerList = (PlayerList) NetworkMessageService.getMessageData(decodedMessage, PlayerList.class);
            gameViewModel.setPlayers(playerList);
        });

        handlers.put(ChangeState.class, decodedMessage -> {
            ChangeState changeState = (ChangeState) NetworkMessageService.getMessageData(decodedMessage, ChangeState.class);

            if (changeState.getNewState().equals(ChangeState.GAME_STATE))
                showGameFragment();
            if (changeState.getNewState().equals(ChangeState.END_STATE))
                showEndActivity();
        });

        handlers.put(ServerTime.class, decodedMessage -> {
            ServerTime serverTime = (ServerTime) NetworkMessageService.getMessageData(decodedMessage, ServerTime.class);
            gameViewModel.setServerTime(serverTime);
        });

        handlers.put(PlayerScoresContainer.class, decodedMessage -> {
           PlayerScoresContainer scoresContainer = (PlayerScoresContainer) NetworkMessageService
                   .getMessageData(decodedMessage, PlayerScoresContainer.class);

           gameViewModel.setPlayerScoreContainer(scoresContainer);
        });

        return handlers;
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
                    // TODO: Go back to the main screen
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

        Consumer<String> handler = messageHandlers.get(messageType);

        if (handler != null) {
            handler.accept(decodedMessage);
        }
    }

    private void showGameFragment() {
        Fragment gameFragment = new Game();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainerView, gameFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void showEndActivity() {
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }

}