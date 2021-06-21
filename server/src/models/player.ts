import { PlayerStructure } from "./player_structure";

export interface Player {
    id: string,
    username: string,
    isLeader: boolean,
    isReady: boolean,
    cookies: number,
    total_cookies: number,
    structures: PlayerStructure[],
};
