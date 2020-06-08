package com.mankomania.game.core.network.messages.servertoclient.horserace;

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
}
