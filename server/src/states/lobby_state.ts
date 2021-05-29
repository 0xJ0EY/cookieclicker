import { Player } from "../models/player";
import Server from "../server";
import State from "./state";

export default class LobbyState implements State {

    onConnect(server: Server, player: Player): void {
        console.log(`Welcome ${player.username}`);    
    }

    onDisconnect(server: Server, player: Player): void {
        console.log(`Goodbye ${player.username}`);
    }

    onMessage(server: Server, player: Player, message: string): void {
    }

    onTick(server: Server): void {
    }

}
