package com.joeyderuiter.cookieclicker.models.scores;

import java.util.List;

import lombok.Getter;

public class PlayerScores {
    @Getter
    private int number;

    @Getter
    private List<PlayerScore> scores;
}
