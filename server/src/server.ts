import { ServerConfig } from "./models/server";
import { registerServer as registerServerInLobbyList, removeServer } from "./util/server";
import express from 'express';
import http from 'http';
import ws from 'ws';
import { URL } from "url";

enum ServerStatus {
    STARTED,
    STOPPED
}

export default class Server {
    status: ServerStatus;
    config: ServerConfig;

    clients: Map<string, ws>;

    httpServer: http.Server;
    wsServer: ws.Server;
    
    constructor(config: ServerConfig) {
        this.status = ServerStatus.STOPPED;
        this.config = config;
        this.clients = new Map();

        this.httpServer = this.setupHttpServer();
        this.wsServer   = this.setupWebsocketServer();
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

            // For production use a token based system, instead of thrusting the client to send the correct user ID 
            if (!url.searchParams.has('id')) {
                socket.write('HTTP/1.1 401 Unauthorized\r\n\r\n');
                socket.destroy();
                return;
            }

            userId = url.searchParams.get('id') as string;
            
            // Create a connected client with the user id
            // Upgrade the connection

            wss.handleUpgrade(req, socket, head, (ws) => {
                wss.emit('connection', ws, req);
            });
        });
        
        wss.on('connection', (ws, request) => {

            this.clients.set(userId, ws);

            ws.on('message', (msg) => {
                console.log(msg);
                
            });

            ws.on('close', () => {
                this.clients.delete(userId);
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
        this.httpServer.listen(8080, () => {
            console.log('Listening on http://localhost:8080');
        });
    }

    update() {
        // console.log('tick');
    }

    private onMessage(requester: string, message: string): void {

    }

    stop() {
        if (this.status !== ServerStatus.STARTED) return;
        console.log('Stopping the server.');
        
        removeServer(this.config);
        this.status = ServerStatus.STOPPED;
    }

    
}