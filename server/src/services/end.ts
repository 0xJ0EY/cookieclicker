import Server from "../server";
import EndData from "../models/end_data";
import { Player } from "../models/player";

export default class EndService {

    public getEndData(player: Player, server: Server): EndData {

        let players: Player[] = [];

        for (let value of server.players.values()) {
            players.push(value);
        }

        return { 
            personalScore: player.total_cookies,
            playerScores: server.scores,
            players
        } as EndData;
    }

}