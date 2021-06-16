import { Player } from "../models/player";
import Server from "../server";
import { NetworkMessage } from "../util/messages";
import State from "./state";

export default class LobbyState implements State {

    private messageHandlers: Map<string, (player: Player, message: NetworkMessage) => void>;
    private server: Server;

    constructor(server: Server) {
        this.messageHandlers = this.setupMessageHandlers();
        this.server = server;
    }

    private setupMessageHandlers(): Map<string, (player: Player, message: NetworkMessage) => void> {
        const handlers = new Map<string, (player: Player, message: NetworkMessage) => void>();

        handlers.set("LobbyVote", (player, message) => {
            this.handleLobbyVote(player);
        })

        handlers.set("LobbyStartGame", (player, message) => {
            console.log('LobbyStartGame');
            this.handleLobbyStartGame();
        });

        return handlers;
    }

    onConnect(player: Player): void {
        console.log(`Welcome ${player.username}`);

        this.server.sharePlayerState();
    }

    onDisconnect(player: Player): void {
        console.log(`Goodbye ${player.username}`);
    }

    onMessage(player: Player, message: NetworkMessage): void {
        console.log(`${player.username}: ${message.objectType}`);

        const messageType = message.objectType;

        console.log(messageType);
        const handler = this.messageHandlers.get(messageType);

        if (handler) {
            handler(player, message);
        }
    }

    private handleLobbyVote(player: Player) {
        player.isReady = true;
        this.server.players.set(player.id, player);

        this.server.sharePlayerState();
    }

    private handleLobbyStartGame() {
        if (!this.canStartGame())
            return;

        this.server.startGame();
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

}
