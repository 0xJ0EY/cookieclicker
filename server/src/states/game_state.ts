import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import State from "./state";

export default class GameState implements State {
    private server: Server;

    constructor(server: Server) {
        this.server = server;

        console.log('starting game');
    }

    onChange(server: Server): void {
        this.server = server;
    }

    onConnect(player: Player): void {
        console.log(`Welcome ${player.username}`);
    }

    onDisconnect(player: Player): void {
        console.log(`Goodbye ${player.username}`);

        if (this.server.players.size === 0)
            this.server.resetGame();
    }

    onMessage(player: Player, message: NetworkMessage): void {
    }

    onTick(): void {
        // Do actions
        this.calculateNewPoints();

        // Send the updated player state
        this.server.sharePlayerState();
    }

    private calculateNewPoints(): void {
        this.server.players.forEach(player => {
            player.points += 1;
        });
    }

}
