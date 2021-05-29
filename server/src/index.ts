import { v4 as uuidv4 } from 'uuid';
import { ServerConfig } from './models/server';
import Server from './server';

const serverId = uuidv4(); 
const serverIp = '10.0.2.2';

const serverHostname = '0.0.0.0';
const serverPort = '8080';

const config: ServerConfig = {
    id: serverId,
    ip: serverIp,
    hostname: serverHostname,
    port: serverPort,
}

const server = new Server(config);

server.start();

function onExit() {
    server.stop();
    process.exit();
}

process.on('exit', onExit);
process.on('SIGINT', onExit);
process.on('SIGUSR1', onExit);
process.on('SIGUSR2', onExit);
