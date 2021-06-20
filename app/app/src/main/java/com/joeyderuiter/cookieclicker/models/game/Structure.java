package com.joeyderuiter.cookieclicker.models.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class Structure {

    @Getter
    private String name;

    @Getter
    @JsonProperty("points_per_second")
    private int pointsPerSecond;

}
