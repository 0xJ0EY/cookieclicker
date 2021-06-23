import { PlayerPowerup } from "./player_powerup";

export interface Player {
    id: string,
    username: string,
    isLeader: boolean,
    isReady: boolean,
    cookies: number,
    total_cookies: number,
    powerups: PlayerPowerup[],
};
