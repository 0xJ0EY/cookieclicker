package com.joeyderuiter.cookieclicker.models.user;

import com.joeyderuiter.cookieclicker.models.game.Powerup;

import lombok.Getter;

public class PlayerPowerup {

    @Getter
    private int amount;

    @Getter
    private Powerup structure;

}
