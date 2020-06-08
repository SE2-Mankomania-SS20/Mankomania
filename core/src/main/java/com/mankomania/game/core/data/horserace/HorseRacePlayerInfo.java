package com.mankomania.game.core.data.horserace;

public class HorseRacePlayerInfo {

    private int playerIndex;
    private int horseIndex;
    private int betAmount;

    public HorseRacePlayerInfo() {
        // empty for kryonet
    }

    public HorseRacePlayerInfo(int playerIndex, int horseIndex, int betAmount) {
        this.playerIndex = playerIndex;
        this.horseIndex = horseIndex;
        this.betAmount = betAmount;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getHorseIndex() {
        return horseIndex;
    }

    public int getBetAmount() {
        return betAmount;
    }
}
