package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

import androidx.annotation.NonNull;

public class LobbyServiceLocator {
    public static LobbyService instance;

    private LobbyServiceLocator() {}

    public synchronized static LobbyService getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new LobbyService();
        }

        return instance;
    }

}

