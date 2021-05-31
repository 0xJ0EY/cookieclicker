import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";

export default interface State {
    onConnect(server: Server, player: Player): void;
    onDisconnect(server: Server, player: Player): void;
    onMessage(server: Server, player: Player, message: NetworkMessage): void;
    onTick(server: Server): void;
}