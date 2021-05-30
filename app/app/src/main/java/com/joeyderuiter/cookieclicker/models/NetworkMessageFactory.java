package com.joeyderuiter.cookieclicker.models;

import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.user.Player;

public class NetworkMessageFactory {
    public static Class<?> fromString(String name) {
        switch (name) {
            case "PlayerList":
                return PlayerList.class;
            case "Player":
                return Player.class;
            default:
                return Void.class;
        }
    }
}
