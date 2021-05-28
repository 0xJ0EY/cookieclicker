import { ServerConfig } from '../models/server';
import { database } from './../database';

export const registerServer = (data: ServerConfig) => {
    database.ref("servers")
        .child(data.id)
        .set(data);
};

export const removeServer = (data: ServerConfig) => {
    database.ref("servers")
        .child(data.id)
        .remove();
};
