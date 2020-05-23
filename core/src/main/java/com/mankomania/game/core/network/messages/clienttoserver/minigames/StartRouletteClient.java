package com.mankomania.game.core.network.messages.clienttoserver.minigames;

public class StartRouletteClient {
    //Message verschickt, wenn ein Spieler auf das Rouletteminispielfeld kommt
    private int playerId;

    public StartRouletteClient(int playerId) {
        this.playerId = playerId;
    }

    public StartRouletteClient() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
