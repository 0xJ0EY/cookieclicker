export interface PlayerScoresContainer {
    playerScores: PlayerScores[];
}

export interface PlayerScores {
    time: number,
    scores: PlayerScore[],
}

export interface PlayerScore {
    playerId: string,
    score: number
}
