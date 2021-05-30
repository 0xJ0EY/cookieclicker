package com.joeyderuiter.cookieclicker.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class Player {
    @Getter
    private String id;

    @Getter
    private String username;

    @Getter
    @JsonProperty("isLeader")
    private String leader;

    @Getter
    @JsonProperty("isReady")
    private String ready;
}
