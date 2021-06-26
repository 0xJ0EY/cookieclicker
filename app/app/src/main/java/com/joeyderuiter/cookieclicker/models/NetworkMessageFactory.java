package com.joeyderuiter.cookieclicker.models;

import com.joeyderuiter.cookieclicker.models.messages.ChangeState;
import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.messages.ServerTime;
import com.joeyderuiter.cookieclicker.models.scores.PlayerScoresContainer;
import com.joeyderuiter.cookieclicker.models.user.Player;

public class NetworkMessageFactory {
    public static Class<?> fromString(String name) {
        switch (name) {
            case "PlayerScoresContainer":
                return PlayerScoresContainer.class;
            case "ServerTime":
                return ServerTime.class;
            case "ChangeState":
                return ChangeState.class;
            case "PlayerList":
                return PlayerList.class;
            case "Player":
                return Player.class;
            default:
                return Void.class;
        }
    }
}
