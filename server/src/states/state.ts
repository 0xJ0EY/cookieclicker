import { Player } from "../models/player";
import { NetworkMessage } from "../util/messages";

export default interface State {
    onConnect(player: Player): void;
    onDisconnect(player: Player): void;
    onMessage(player: Player, message: NetworkMessage): void;
    onTick(currentTick: number): void;
}
