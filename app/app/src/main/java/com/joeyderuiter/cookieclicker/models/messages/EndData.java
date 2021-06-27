package com.joeyderuiter.cookieclicker.models.messages;

import com.joeyderuiter.cookieclicker.models.scores.PlayerScores;
import com.joeyderuiter.cookieclicker.models.user.Player;

import java.util.List;

import lombok.Getter;

public class EndData {

    @Getter
    private int personalScore;

    @Getter
    private List<Player> players;

    @Getter
    private List<PlayerScores> playerScores;

}
