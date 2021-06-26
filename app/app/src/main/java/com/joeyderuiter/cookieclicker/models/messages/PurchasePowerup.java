package com.joeyderuiter.cookieclicker.models.messages;

import com.joeyderuiter.cookieclicker.models.game.Powerup;

import lombok.Getter;

public class PurchasePowerup {
    @Getter
    private final Powerup powerup;

    public PurchasePowerup(Powerup powerup) {
        this.powerup = powerup;
    }
}
