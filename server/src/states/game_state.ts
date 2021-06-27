import ChangeState from "../models/network/change_state";
import { Player } from "../models/player";
import { PlayerPowerup } from "../models/player_powerup";
import { PlayerScore, PlayerScores, PlayerScoresContainer } from "../models/player_score";
import { Powerup } from "../models/powerup";
import PurchasePowerup from "../models/purchase_powerup";
import { ServerTime } from "../models/server_time";
import Server from "../server";
import PowerupService from "../services/powerups";
import { NetworkMessage } from "../util/messages";
import { calculateTicksFromSeconds, isCurrentTick } from "../util/tick_rate";
import State from "./state";

export default class GameState implements State {
    
    private serverTime: ServerTime;
    private messageHandlers: Map<string, (player: Player, message: NetworkMessage) => void>;
    private server: Server;
    private powerupService: PowerupService;

    constructor(server: Server) {
        this.messageHandlers = this.setupMessageHandlers();
        this.serverTime = { timeLeft: 120, startTime: 120 } as ServerTime;
        this.server = server;
        this.powerupService = new PowerupService();

        server.sendToAll("ChangeState", new ChangeState("GameState"));

        console.log('starting game');
    }

    private setupMessageHandlers(): Map<string, (player: Player, message: NetworkMessage) => void> {
        const handlers = new Map<string, (player: Player, message: NetworkMessage) => void>();

        handlers.set("CookieClick", (player, message) => {
            this.incrementScore(player);
        }); 

        handlers.set("PurchasePowerup", (player, message) => {
            const purchasePowerup: PurchasePowerup = message.object;

            this.purchasePowerup(player, purchasePowerup.powerup);
        });

        return handlers;
    }

    private incrementScore(player: Player): void {
        const serverPlayer = this.server.players.get(player.id);
        if (!serverPlayer) return;

        const cookiesPerClick = this.powerupService.calculateCookiesPerClick(serverPlayer);

        serverPlayer.cookies += cookiesPerClick;
        serverPlayer.total_cookies += cookiesPerClick;
    }

    private purchasePowerup(player: Player, powerup: Powerup): void {
        const serverPlayer = this.server.players.get(player.id);
        if (!serverPlayer) return;

        console.log(powerup);
    
        
        // Fetch the correct cookie cost from the store
        const cost = this.powerupService.getCost(serverPlayer, powerup.id);
        const serverPowerup = this.powerupService.getPowerupById(powerup.id);

        // Check if the player has the cookies to buy it
        if (serverPlayer.cookies < cost) return;

        serverPlayer.cookies -= cost;

        this.addPowerup(serverPlayer, serverPowerup);
    }

    private addPowerup(player: Player, powerup: Powerup) {
        let found = false;

        player.powerups.forEach(playerPowerup => {
            if (playerPowerup.powerup.id === powerup.id) {
                playerPowerup.amount += 1;

                found = true;
            } 
        });

        if (!found)
            player.powerups.push({ amount: 1, powerup } as PlayerPowerup)
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

            if (this.serverTime.timeLeft <= 0)
                this.server.endGame();
        }

        if (isCurrentTick(currentTick, calculateTicksFromSeconds(1.0))) {
            this.storePlayerScore();
        }

        // Send the updated player state
        this.server.sharePlayerState();
    }

    private storePlayerScore(): void {

        const scores: PlayerScore[] = [];

        this.server.players.forEach(player => {
            const score: PlayerScore = {
                score: player.total_cookies,
                playerId: player.id
            };

            scores.push(score);
        });

        const time = this.server.scores.length;

        const playerScores: PlayerScores = { time, scores };
        this.server.scores.push(playerScores);

        console.log('Sending current player scores');

        this.server.sendToAll("PlayerScoresContainer", { playerScores: this.server.scores } as PlayerScoresContainer);
    }

    private calculateNewPoints(): void {
        this.server.players.forEach(player => {
            // player.points += 1;
        });
    }

}
