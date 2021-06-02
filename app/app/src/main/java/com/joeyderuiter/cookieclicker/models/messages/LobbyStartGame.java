package com.joeyderuiter.cookieclicker.models.messages;

import lombok.Getter;

public class LobbyStartGame {

    @Getter
    private final boolean start;

    public LobbyStartGame() { start = true; }

}
