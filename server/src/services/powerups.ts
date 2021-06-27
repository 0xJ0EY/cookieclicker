import { Player } from "../models/player";
import { Powerup } from "../models/powerup";
import ShopPowerup from "../models/shop_powerup";

export default class PowerupService {
    
    public listAll(player: Player): ShopPowerup[] {

        return [
            {
                cost: this.calculateCost(10, 1, player),
                resource: "bronze_dagger",
                powerup: { id: 1, name: "Bronze dagger", pointsPerClick: 1 }
            },
            {
                cost: this.calculateCost(2_00, 2, player),
                resource: "steel_dagger",
                powerup: { id: 2, name: "Steel dagger", pointsPerClick: 10 }
            },
            {
                cost: this.calculateCost(1_000, 3, player),
                resource: "mithril_dagger",
                powerup: { id: 3, name: "Mithril dagger", pointsPerClick: 20 }
            },
            {
                cost: this.calculateCost(5_000, 4, player),
                resource: "adamant_dagger",
                powerup: { id: 5, name: "Adamant dagger", pointsPerClick: 50 }
            },
            {
                cost: this.calculateCost(15_000, 5, player),
                resource: "rune_dagger",
                powerup: { id: 5, name: "Rune dagger", pointsPerClick:  75 }
            },
            {
                cost: this.calculateCost(50_000, 6, player),
                resource: "dragon_dagger",
                powerup: { id: 5, name: "Dragon dagger", pointsPerClick: 100 }
            }
        ];

    }

    public getPowerupById(powerupId: number): Powerup {
        const all = this.listAll({} as Player);

        for (let entry of all) {
            if (entry.powerup.id == powerupId) {
                return entry.powerup;
            }
        }
        
        throw Error("Invalid powerup id");
    }

    public calculateCookiesPerClick(player: Player): number {
        let result = 1;

        player.powerups.forEach(entry => {
            const points = entry.amount * entry.powerup.pointsPerClick;

            result += points;
        });

        return result;
    }

    public getCost(player: Player, powerupId: number): number {
        const all = this.listAll(player);

        for (let entry of all) {
            if (entry.powerup.id == powerupId) {
                return entry.cost;
            }
        }

        throw Error("Invalid powerup id");
    }

    private calculateCost(basePrice: number, powerupId: number, player: Player): number {
        let amount = this.getAmountFromPlayerByPowerupId(player, powerupId);
        amount += 1;

        let price = basePrice;

        for (let i = 1; i <= amount; i++) {
            price *= i;
        }
        
        return price;
    }

    private getAmountFromPlayerByPowerupId(player: Player, id: number): number {
        if (!player.powerups) return 0; 

        for (let entry of player.powerups) {
            if (entry.powerup.id == id) {
                return entry.amount;
            }
        }

        return 0;
    }

}