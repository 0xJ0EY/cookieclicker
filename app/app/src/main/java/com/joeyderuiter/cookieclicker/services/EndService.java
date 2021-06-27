package com.joeyderuiter.cookieclicker.services;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyderuiter.cookieclicker.models.game.ShopPowerup;
import com.joeyderuiter.cookieclicker.models.lobby.Server;
import com.joeyderuiter.cookieclicker.models.messages.EndData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EndService {
    private static final String TAG = "StoreService";
    private Server server;

    private final AuthService authService;

    public EndService(Context context) {
        authService = AuthServiceLocator.getInstance(context);
    }

    public void configure(Server server) {
        this.server = server;
    }

    public EndData fetchEndData() {

        OkHttpClient client = new OkHttpClient();
        String url = "http://" + server.ip + ":" + server.port + "/end";

        Request request = new Request.Builder()
                .header("X-Player-Id", authService.getCurrentUserId())
                .url(url)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Do the call in another thread
        Callable<EndData> callable = () -> {
            Response response = client.newCall(request).execute();
            String body = response.body().string();

            // Map the response to the list
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(body, EndData.class);
        };

        Future<EndData> future = executor.submit(callable);

        try {
            // Execute the future, and if it crashes log the error
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }


}
