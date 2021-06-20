package com.joeyderuiter.cookieclicker.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;

public class Player {
    @Getter
    private String id;

    @Getter
    private String username;

    @Getter
    @JsonProperty("isLeader")
    private boolean leader;

    @Getter
    @JsonProperty("isReady")
    private boolean ready;

    @Getter
    private int points;

    @Getter
    private List<PlayerStructure> structures;
}
