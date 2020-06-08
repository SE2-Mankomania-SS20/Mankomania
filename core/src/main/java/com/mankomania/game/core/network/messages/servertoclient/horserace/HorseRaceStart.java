package com.mankomania.game.core.network.messages.servertoclient.horserace;

public class HorseRaceStart {
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
