package com.mankomania.game.core.network.messages.servertoclient.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class EndTrickyOne {

    private int playerIndex;

    //only used to inform clients that MiniGame has ended and they can switch back to main Screen

    public EndTrickyOne() {
    }

    public EndTrickyOne(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
