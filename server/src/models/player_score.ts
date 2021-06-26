export interface PlayerScoresContainer {
    playerScores: PlayerScores[];
}

export interface PlayerScores {
    time: number,
    scores: PlayerScore[],
}

export interface PlayerScore {
    player: string,
    score: number
}
