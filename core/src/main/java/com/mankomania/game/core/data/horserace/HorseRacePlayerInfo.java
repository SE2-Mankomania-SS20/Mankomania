package com.mankomania.game.core.data.horserace;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseRacePlayerInfo)) return false;
        HorseRacePlayerInfo that = (HorseRacePlayerInfo) o;
        return getPlayerIndex() == that.getPlayerIndex() &&
                getHorseIndex() == that.getHorseIndex() &&
                getBetAmount() == that.getBetAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerIndex(), getHorseIndex(), getBetAmount());
    }
}
