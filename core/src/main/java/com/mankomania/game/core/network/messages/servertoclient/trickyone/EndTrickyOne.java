package com.mankomania.game.core.network.messages.servertoclient.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class EndTrickyOne {

    private int playerIndex;
    private int amountWinLose;

    //only used to inform clients that MiniGame has ended and they can switch back to main Screen

    public EndTrickyOne() {
    }

    public EndTrickyOne(int playerIndex, int amountWinLose) {
        this.playerIndex = playerIndex;
        this.amountWinLose = amountWinLose;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getAmountWinLose() {
        return amountWinLose;
    }

    public void setAmountWinLose(int amountWinLose) {
        this.amountWinLose = amountWinLose;
    }
}
