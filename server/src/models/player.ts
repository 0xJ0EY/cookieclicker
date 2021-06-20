import PlayerStructure from "./player_structure";

export interface Player {
    id: string,
    username: string,
    isLeader: boolean,
    isReady: boolean,
    points: number,
    structures: PlayerStructure[],
};
