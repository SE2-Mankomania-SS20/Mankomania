package com.mankomania.game.core.network.messages.servertoclient.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class StartTrickyOne {
    private int playerIndex;

    public StartTrickyOne() {
    }

    public StartTrickyOne(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
