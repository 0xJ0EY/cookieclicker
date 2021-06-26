package com.joeyderuiter.cookieclicker.models.messages;

import lombok.Getter;

public class ChangeState {
    public static String GAME_STATE = "GameState";
    public static String END_STATE = "EndState";

    @Getter
    private String newState;

}
