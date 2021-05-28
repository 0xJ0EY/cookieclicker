import { ServerConfig } from "./models/server";
import { registerServer as registerServerInLobbyList, removeServer } from "./util/server";
import WebSocket from 'ws';

enum ServerStatus {
    STARTED,
    STOPPED
}

export default class Server {
    status: ServerStatus;

    config: ServerConfig;
    socket: any;
    
    constructor(config: ServerConfig) {
        this.status = ServerStatus.STOPPED;

        this.config = config;
        this.socket = new WebSocket.Server({
            port: 8080
        });
    }

    start() {
        // Print the starting message
        console.log('Starting the server.');

        // Register the server in the Firebase server browser
        registerServerInLobbyList(this.config);
        
        // Setup the tick rate for the game
        setInterval(() => {
            this.update();
        }, 1000);

        this.status = ServerStatus.STARTED;
    }

    update() {
        console.log('tick');
    }

    stop() {
        if (this.status !== ServerStatus.STARTED) return;
        console.log('Stopping the server.');
        
        removeServer(this.config);
        this.status = ServerStatus.STOPPED;
    }
}