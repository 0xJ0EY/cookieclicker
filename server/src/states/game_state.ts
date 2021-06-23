import { Player } from "../models/player";
import { ServerTime } from "../models/server_time";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import { calculateTicksFromSeconds, isCurrentTick } from "../util/tick_rate";
import State from "./state";

export default class GameState implements State {
    
    private serverTime: ServerTime;
    private messageHandlers: Map<string, (player: Player, message: NetworkMessage) => void>;
    private server: Server;

    constructor(server: Server) {
        this.messageHandlers = this.setupMessageHandlers();
        this.serverTime = { timeLeft: 240, startTime: 240 } as ServerTime;
        this.server = server;

        console.log('starting game');
    }

    private setupMessageHandlers(): Map<string, (player: Player, message: NetworkMessage) => void> {
        const handlers = new Map<string, (player: Player, message: NetworkMessage) => void>();

        handlers.set("CookieClick", (player, message) => {
            this.incrementScore(player);
        }); 

        return handlers;
    }

    private incrementScore(player: Player): void {
        const serverPlayer = this.server.players.get(player.id);
        if (!serverPlayer) return;

        serverPlayer.cookies += 1;
        serverPlayer.total_cookies += 1;
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
        const messageType = message.objectType;
        const handler = this.messageHandlers.get(messageType);

        if (handler) {
            handler(player, message);
        }
    }

    onTick(currentTick: number): void {
        this.calculateNewPoints();

        if (isCurrentTick(currentTick, calculateTicksFromSeconds(1.0))) {
            this.serverTime.timeLeft -= 1;
            this.server.sendToAll('ServerTime', this.serverTime);
        }

        // Send the updated player state
        this.server.sharePlayerState();
    }

    private calculateNewPoints(): void {
        this.server.players.forEach(player => {
            // player.points += 1;
        });
    }

}
