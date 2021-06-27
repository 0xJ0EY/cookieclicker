import { Player } from "./player";
import { PlayerScores } from "./player_score";

export default interface EndData {
    personalScore: number,
    players: Player[],
    playerScores: PlayerScores[],
}
