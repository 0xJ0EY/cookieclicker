import { Player } from '../models/player';
import { database } from './../database';

export default class AuthenticationService {
    
    async getPlayer(playerId: string): Promise<Player> {
        return database.ref("profiles").child(playerId).get().then(snapshot => {
            return snapshot.val();
        });
    }

}
