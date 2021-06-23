package com.joeyderuiter.cookieclicker.models.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class Powerup {

    @Getter
    private String name;

    @Getter
    private int pointsPerClick;

}
