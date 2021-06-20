import Server from "../server";

export const calculateTicksFromSeconds = (seconds: number): number => {
    const time = seconds * 1000;

    return Math.round(time / Server.TICK_RATE);
}
