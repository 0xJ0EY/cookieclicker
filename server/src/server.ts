import { ServerConfig } from "./models/server";
import { registerServer as registerServerInLobbyList, removeServer } from "./util/server";
import express from 'express';
import http from 'http';
import ws from 'ws';
import { URL } from "url";
import AuthenticationService from "./services/authentication";
import { Player } from "./models/player";
import State from "./states/state";
import LobbyState from "./states/lobby_state";
import { decodeMessage, encodeMessage } from "./util/messages";
import WebSocket from "ws";
import GameState from "./states/game_state";
import PlayerList from "./models/network/player_list";
import ChangeState from "./models/network/change_state";
import PowerupService from "./services/powerups";

enum ServerStatus {
    STARTED,
    STOPPED
}

export default class Server {
    public static readonly TICK_RATE = 200;

    private status: ServerStatus;
    private config: ServerConfig;

    public players: Map<string, Player>;
    private clients: Map<string, ws>;

    private httpServer: http.Server;
    private wsServer: ws.Server;

    private authenticationService: AuthenticationService;
    private powerupService: PowerupService;

    private state: State;
    private currentTick: number = 0;
    
    constructor(config: ServerConfig) {
        this.status = ServerStatus.STOPPED;
        this.config = config;
        this.players = new Map();
        this.clients = new Map();
        this.state = new LobbyState(this);

        this.httpServer = this.setupHttpServer();
        this.wsServer   = this.setupWebsocketServer();

        this.authenticationService = new AuthenticationService();
        this.powerupService = new PowerupService();
    }

    private setupHttpServer(): http.Server {
        const app = express();

        app.get('/', (req, res) => {
            res.send('Hello gamers :^)');
        })

        app.get('/shop/powerups', (req, res) => {
            console.log(req);

            const playerId = req.header('X-Player-Id');
            if (playerId == null)
                return res.send([]);

            const player = this.players.get(playerId);
            if (player == null)
                return res.send([]);

            res.send(this.powerupService.listAll(player));
        });

        return http.createServer(app);
    }

    private setupWebsocketServer(): ws.Server {
        const wss = new ws.Server({ clientTracking: false, noServer: true });

        this.httpServer.on('upgrade', (req, socket, head) => {
            const url = new URL('http://127.0.0.1/' + req.url);

            const userId = url.searchParams.get('id') as string;

            // Create a connected client with the user id
            this.authenticationService
                .getPlayer(userId)
                .then(player => {
                    if (player == null) {
                        socket.write('HTTP/1.1 401 Unauthorized\r\n\r\n');
                        socket.destroy();
                        return;
                    }

                    player.id           = userId;
                    player.isLeader     = this.players.size == 0;
                    player.isReady      = false;
                    player.cookies      = 0;
                    player.total_cookies = 0;
                    player.powerups     = [];

                    this.players.set(userId, player);

                    // Upgrade the connection
                    wss.handleUpgrade(req, socket, head, (ws) => {
                        wss.emit('connection', ws, req, userId);
                    });
                });
        });
        
        wss.on('connection', (ws: WebSocket, request: http.IncomingMessage, userId: string) => {

            this.clients.set(userId, ws);


            const player = this.players.get(userId) as Player;
            this.players.set(userId, player);
            this.state.onConnect(player);

            ws.on('message', (msg) => {
                this.onMessage(userId, msg.toString());
            });

            ws.on('close', () => {
                const player = this.players.get(userId);
                if (player) {
                    this.state.onDisconnect(player);
                }

                this.clients.delete(userId);
                this.players.delete(userId);
            });
        });

        return wss;
    }

    start() {
        // Print the starting message
        console.log('Starting the server.');

        // Register the server in the Firebase server browser
        registerServerInLobbyList(this.config);

        // Start the HTTP & WS server
        this.startWebsocketServer();
        
        // Setup the tick rate for the game
        setInterval(() => {
            this.update();
        }, Server.TICK_RATE);

        this.status = ServerStatus.STARTED;
    }

    private startWebsocketServer() {
        this.httpServer.listen(8080, '0.0.0.0', () => {
            console.log('Listening on http://localhost:8080');
        });
    }

    update() {
        this.state.onTick(this.currentTick);
        this.currentTick++;
    }

    private onMessage(userId: string, message: string): void {
        const player = this.players.get(userId);
        if (!player) return;

        const networkMessage = decodeMessage(message);

        console.log(`${player.username}: ${JSON.stringify(networkMessage)}`);

        this.state.onMessage(player, networkMessage);
    }

    stop() {
        if (this.status !== ServerStatus.STARTED) return;
        
        console.log('Stopping the server.');
        
        removeServer(this.config);

        this.status = ServerStatus.STOPPED;
    }

    resetGame() {
        console.log('Resetting the server.');

        this.state = new LobbyState(this);

        registerServerInLobbyList(this.config);
        
        this.status = ServerStatus.STARTED;
    }

    startGame() {
        this.state = new GameState(this);

        this.sendToAll("ChangeState", new ChangeState("GameState"));

        // Remove the server from the serverlist
        removeServer(this.config);

        this.status = ServerStatus.STARTED;
    }

    sendToPlayer(playerId: string, objectType: string, object: any) {
        const message = encodeMessage({objectType, object});

        const client = this.clients.get(playerId);
        client?.send(message);
    }

    sendToAll(objectType: string, object: any) {
        const message = encodeMessage({objectType, object});

        this.clients.forEach(client => {
            client?.send(message);
        });
    }

    sharePlayerState(): void {
        const players: Player[] = [];

        this.players.forEach(player => {
            players.push(player);
        });

        this.sendToAll('PlayerList', { players } as PlayerList);
    }

}