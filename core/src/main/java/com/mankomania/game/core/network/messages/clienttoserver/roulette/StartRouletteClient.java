package com.mankomania.game.core.network.messages.clienttoserver.roulette;

public class StartRouletteClient {
    //Message verschickt, wenn ein Spieler auf das Rouletteminispielfeld kommt
    private int playerIndex;

    public StartRouletteClient(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public StartRouletteClient() {
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
