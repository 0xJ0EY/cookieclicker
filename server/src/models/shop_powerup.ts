import { Powerup } from "./powerup";

export default interface ShopPowerup {
    cost: number,
    resource: string,
    powerup: Powerup
}