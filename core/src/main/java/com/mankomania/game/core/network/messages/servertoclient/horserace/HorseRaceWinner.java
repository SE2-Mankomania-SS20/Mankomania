package com.mankomania.game.core.network.messages.servertoclient.horserace;

import java.util.Objects;

public class HorseRaceWinner {
    int horseIndex;

    public HorseRaceWinner() {
        // empty for kryonet
    }

    public HorseRaceWinner(int horseIndex) {
        this.horseIndex = horseIndex;
    }

    public int getHorseIndex() {
        return horseIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseRaceWinner)) return false;
        HorseRaceWinner that = (HorseRaceWinner) o;
        return getHorseIndex() == that.getHorseIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHorseIndex());
    }
}
