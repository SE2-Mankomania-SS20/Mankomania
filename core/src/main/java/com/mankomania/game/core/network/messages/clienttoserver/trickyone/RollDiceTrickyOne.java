package com.mankomania.game.core.network.messages.clienttoserver.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class RollDiceTrickyOne {

    private int playerIndex;

    //used to trigger roll Action on server
    public RollDiceTrickyOne() {
    }

    public RollDiceTrickyOne(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
