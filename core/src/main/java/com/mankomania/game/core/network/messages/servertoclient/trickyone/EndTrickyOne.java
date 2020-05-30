package com.mankomania.game.core.network.messages.servertoclient.trickyone;

import com.mankomania.game.core.network.messages.clienttoserver.trickyone.TrickyOneBaseMessage;

import java.util.Objects;

/*
 Created by Fabian Oraze on 23.05.20
*/

public class EndTrickyOne extends TrickyOneBaseMessage {

    private int amountWinLose;

    //only used to inform clients that MiniGame has ended and they can switch back to main Screen

    public EndTrickyOne() {
    }

    public EndTrickyOne(int playerIndex, int amountWinLose) {
        this.playerIndex = playerIndex;
        this.amountWinLose = amountWinLose;
    }

    public int getAmountWinLose() {
        return amountWinLose;
    }

    public void setAmountWinLose(int amountWinLose) {
        this.amountWinLose = amountWinLose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndTrickyOne that = (EndTrickyOne) o;
        return playerIndex == that.playerIndex &&
                amountWinLose == that.amountWinLose;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex, amountWinLose);
    }
}
