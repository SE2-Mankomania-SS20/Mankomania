package com.mankomania.game.core.network.messages.servertoclient.horserace;

import java.util.Objects;

public class HorseRaceStart {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseRaceStart)) return false;
        HorseRaceStart that = (HorseRaceStart) o;
        return getCurrentPlayerIndex() == that.getCurrentPlayerIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentPlayerIndex());
    }

    private int currentPlayerIndex;

    public HorseRaceStart() {
        // empty for kryonet
    }

    public HorseRaceStart(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
