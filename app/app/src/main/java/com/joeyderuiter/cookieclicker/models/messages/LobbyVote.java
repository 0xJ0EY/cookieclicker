package com.joeyderuiter.cookieclicker.models.messages;

import lombok.Getter;

public class LobbyVote {

    @Getter
    private final boolean ready;

    public LobbyVote() { ready = true; }

}
