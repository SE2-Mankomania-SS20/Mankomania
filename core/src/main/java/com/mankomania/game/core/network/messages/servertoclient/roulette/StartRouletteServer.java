package com.mankomania.game.core.network.messages.servertoclient.roulette;

import java.util.Objects;

public class StartRouletteServer {
    //Nachricht von Server an Clients, dass das Rouletteminigame startet
    private int playerIndex;

    public StartRouletteServer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public StartRouletteServer() {
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartRouletteServer that = (StartRouletteServer) o;
        return playerIndex == that.playerIndex;
    }
    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }
}
