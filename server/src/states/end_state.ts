import Server from "../server";
import { Player } from "../models/player";
import { NetworkMessage } from "../util/messages";
import State from "./state";
import ChangeState from "../models/network/change_state";

export default class EndState implements State {

    constructor(server: Server) {
        server.sendToAll("ChangeState", new ChangeState("EndState"));
    }

    onConnect(player: Player): void {}
    onDisconnect(player: Player): void {}
    onMessage(player: Player, message: NetworkMessage): void {}
    onTick(currentTick: number): void {}

}