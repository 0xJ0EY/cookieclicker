package com.joeyderuiter.cookieclicker.models.messages;

import lombok.Getter;

public class ChangeState {
    public static String GAME_STATE = "GameState";

    @Getter
    private String newState;

}
