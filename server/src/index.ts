import { v4 as uuidv4 } from 'uuid';
import { ServerConfig } from './models/server';
import Server from './server';

const serverId = uuidv4(); 
const serverIp = '127.0.0.1';

const config: ServerConfig = {
    id: serverId,
    ip: serverIp,
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
