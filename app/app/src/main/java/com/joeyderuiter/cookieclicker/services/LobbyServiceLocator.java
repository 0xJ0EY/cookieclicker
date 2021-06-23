package com.joeyderuiter.cookieclicker.services;

public class LobbyServiceLocator {
    public static LobbyService instance;

    private LobbyServiceLocator() {}

    public synchronized static LobbyService getInstance() {
        if (instance == null) {
            instance = new LobbyService();
        }

        return instance;
    }

}

