package com.mankomania.game.core.network.messages.servertoclient.minigames;

public class StartRouletteServer {
    //Nachricht von Server an Clients, dass das Rouletteminigame startet
    private int playerId;

    public StartRouletteServer(int playerId) {
        this.playerId = playerId;
    }

    public StartRouletteServer() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
