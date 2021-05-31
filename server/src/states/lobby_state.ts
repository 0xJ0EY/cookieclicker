import PlayerList from "../models/network/player_list";
import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import State from "./state";

export default class LobbyState implements State {

    onConnect(server: Server, player: Player): void {
        console.log(`Welcome ${player.username}`);

        this.sendLobbyState(server);
    }

    onDisconnect(server: Server, player: Player): void {
        console.log(`Goodbye ${player.username}`);
    }

    onMessage(server: Server, player: Player, message: NetworkMessage): void {
        switch (message.objectType) {
            case "LobbyVote": {
                this.handleLobbyVote(server, player);
            }
        }

        console.log(`${player.username}: ${message.objectType}`);
        
    }

    private handleLobbyVote(server: Server, player: Player) {
        player.isReady = true;
        server.players.set(player.id, player);

        this.sendLobbyState(server);
    }

    onTick(server: Server): void {
    }

    private sendLobbyState(server: Server): void {
        const players: Player[] = [];

        server.players.forEach(player => {
            players.push(player);
        });

        server.sendToAll('PlayerList', { players } as PlayerList);
    }

}
