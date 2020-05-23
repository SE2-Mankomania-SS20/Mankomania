package com.mankomania.game.core.network.messages.clienttoserver.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class StopRollingDice {

    private int playerIndex;

    //used to inform server that you want to stop rolling and end the game
    public StopRollingDice() {
    }

    public StopRollingDice(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }


}
