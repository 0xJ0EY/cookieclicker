import PlayerList from "../models/network/player_list";
import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import State from "./state";

export default class LobbyState implements State {

    private server: Server;

    constructor(server: Server) {
        this.server = server;
    }

    onConnect(player: Player): void {
        console.log(`Welcome ${player.username}`);

        this.sendLobbyState();
    }

    onDisconnect(player: Player): void {
        console.log(`Goodbye ${player.username}`);
    }

    onMessage(player: Player, message: NetworkMessage): void {
        switch (message.objectType) {
            case "LobbyVote": {
                this.handleLobbyVote(player);
            }
            case "LobbyStartGame": {
                this.handleLobbyStartGame();
            }
        }

        console.log(`${player.username}: ${message.objectType}`);
    }

    private handleLobbyVote(player: Player) {
        player.isReady = true;
        this.server.players.set(player.id, player);

        this.sendLobbyState();
    }

    private handleLobbyStartGame() {
        if (this.canStartGame())
            return;

        
    }

    private canStartGame(): boolean {
        const playerCount = this.server.players.size;
        let playersReady = 0;

        this.server.players.forEach(player => {
            playersReady += player.isReady ? 1 : 0;
        });

        return playerCount === playersReady;
    }

    onTick(): void {
    }

    private sendLobbyState(): void {
        const players: Player[] = [];

        this.server.players.forEach(player => {
            players.push(player);
        });

        this.server.sendToAll('PlayerList', { players } as PlayerList);
    }

}
