import { Player } from "../models/player";
import Server from "../server";

export default interface State {
    onConnect(server: Server, player: Player): void;
    onDisconnect(server: Server, player: Player): void;
    onMessage(server: Server, player: Player, message: string): void;
    onTick(server: Server): void;
}