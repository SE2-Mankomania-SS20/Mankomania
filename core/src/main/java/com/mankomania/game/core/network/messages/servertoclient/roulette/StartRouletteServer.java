package com.mankomania.game.core.network.messages.servertoclient.roulette;

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
}
