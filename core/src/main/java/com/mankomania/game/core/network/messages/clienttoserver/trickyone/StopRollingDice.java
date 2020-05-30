package com.mankomania.game.core.network.messages.clienttoserver.trickyone;

/*
 Created by Fabian Oraze on 23.05.20
*/

public class StopRollingDice extends TrickyOneBaseMessage {

    //used to inform server that you want to stop rolling and end the game
    public StopRollingDice() {
    }

    public StopRollingDice(int playerIndex) {
        this.playerIndex = playerIndex;
    }

}
