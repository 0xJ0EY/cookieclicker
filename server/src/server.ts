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

enum ServerStatus {
    STARTED,
    STOPPED
}

export default class Server {
    private status: ServerStatus;
    private config: ServerConfig;

    public players: Map<string, Player>;
    private clients: Map<string, ws>;

    private httpServer: http.Server;
    private wsServer: ws.Server;

    private authenticationService: AuthenticationService;

    private state: State;
    
    constructor(config: ServerConfig) {
        this.status = ServerStatus.STOPPED;
        this.config = config;
        this.players = new Map();
        this.clients = new Map();
        this.state = new LobbyState();

        this.httpServer = this.setupHttpServer();
        this.wsServer   = this.setupWebsocketServer();

        this.authenticationService = new AuthenticationService();
    }

    private setupHttpServer(): http.Server {
        const app = express();

        app.get('/', (req, res) => {
            res.send('Hello world');
        })

        return http.createServer(app);
    }

    private setupWebsocketServer(): ws.Server {
        const wss = new ws.Server({ clientTracking: false, noServer: true });

        let userId: string;

        this.httpServer.on('upgrade', (req, socket, head) => {
            const url = new URL('http://127.0.0.1/' + req.url);

            userId = url.searchParams.get('id') as string;

            // Create a connected client with the user id
            this.authenticationService
                .getPlayer(userId)
                .then(player => {
                    if (player == null) {
                        socket.write('HTTP/1.1 401 Unauthorized\r\n\r\n');
                        socket.destroy();
                        return;
                    }

                    player.isLeader = this.players.size == 0;
                    player.isReady  = false;

                    this.players.set(userId, player);

                    // Upgrade the connection
                    wss.handleUpgrade(req, socket, head, (ws) => {
                        wss.emit('connection', ws, req);
                    });
                });
        });
        
        wss.on('connection', (ws, request) => {

            this.clients.set(userId, ws);


            const player = this.players.get(userId) as Player;
            this.players.set(userId, player);
            this.state.onConnect(this, player);

            ws.on('message', (msg) => {
                this.onMessage(userId, msg.toString());
            });

            ws.on('close', () => {
                const player = this.players.get(userId);
                if (player) {
                    this.state.onDisconnect(this, player);
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
        }, 1000);

        this.status = ServerStatus.STARTED;
    }

    private startWebsocketServer() {
        this.httpServer.listen(8080, '0.0.0.0', () => {
            console.log('Listening on http://localhost:8080');
        });
    }

    update() {
        this.state.onTick(this);
    }

    private onMessage(userId: string, message: string): void {
        const player = this.players.get(userId);
        if (!player) return;

        this.state.onMessage(this, player, message);
    }

    stop() {
        if (this.status !== ServerStatus.STARTED) return;
        console.log('Stopping the server.');
        
        removeServer(this.config);
        this.status = ServerStatus.STOPPED;
    }

    sendToPlayer(playerId: string, objectType: string, object: any) {
        const message = encodeMessage({objectType, object});

        const client = this.clients.get(playerId);
        client?.send(message);
    }

    sendToAll(objectType: string, object: any) {
        const message = encodeMessage({objectType, object});

        this.clients.forEach(client => {

            console.log(message);
            
            client?.send(message);
        });
    }    
}