import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import State from "./state";

export default class GameState implements State {
    private server: Server;

    constructor(server: Server) {
        this.server = server;
    }

    onChange(server: Server): void {
        this.server = server;
    }

    onConnect(player: Player): void {
        console.log(`Welcome ${player.username}`);
    }

    onDisconnect(player: Player): void {
        console.log(`Goodbye ${player.username}`);
    }

    onMessage(player: Player, message: NetworkMessage): void {
    }

    onTick(): void {
    }

}