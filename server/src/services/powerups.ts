import { Player } from "../models/player";
import { Powerup } from "../models/powerup";
import ShopPowerup from "../models/shop_powerup";

export default class PowerupService {
    
    listAll(player: Player): ShopPowerup[] {

        return [
            {
                cost: this.calculateCost(10, 1, player),
                resource: "",
                powerup: { id: 1, name: "Bronze dagger", pointsPerClick: 1 }
            },
            {
                cost: this.calculateCost(25, 2, player),
                resource: "",
                powerup: { id: 2, name: "Steel dagger", pointsPerClick: 5 }
            },
            {
                cost: this.calculateCost(100, 3, player),
                resource: "",
                powerup: { id: 3, name: "Mithril dagger", pointsPerClick: 20 }
            },
            {
                cost: this.calculateCost(250, 4, player),
                resource: "",
                powerup: { id: 5, name: "Adamant dagger", pointsPerClick: 50 }
            },
            {
                cost: this.calculateCost(500, 4, player),
                resource: "",
                powerup: { id: 5, name: "Rune dagger", pointsPerClick: 100 }
            }
        ];

    }

    private calculateCost(basePrice: number, powerupId: number, player: Player): number {
        return basePrice;
    }

    private getAmountFromPlayerByPowerupId(player: Player, id: number) {
        player.powerups.forEach(p => {
            if (p.powerup.id == id) {
                return p.amount;
            }
        });
    }

}