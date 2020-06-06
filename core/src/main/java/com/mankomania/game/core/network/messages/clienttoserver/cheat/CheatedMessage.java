package com.mankomania.game.core.network.messages.clienttoserver.cheat;

/*
 Created by Fabian Oraze on 02.06.20
 */

public class CheatedMessage {

    private int playerIndex;

    public CheatedMessage() {
    }

    public CheatedMessage(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
