package com.mankomania.game.core.network.messages.servertoclient.trickyone;

import com.mankomania.game.core.network.messages.clienttoserver.trickyone.TrickyOneBaseMessage;

import java.util.Objects;

/*
 Created by Fabian Oraze on 23.05.20
*/

public class CanRollDiceTrickyOne extends TrickyOneBaseMessage {

    private int firstDice;
    private int secondDice;
    private int pot;
    private int rolledAmount;

    public CanRollDiceTrickyOne() {
    }

    public CanRollDiceTrickyOne(int playerIndex, int firstDice, int secondDice, int pot, int rolledAmount) {
        this.playerIndex = playerIndex;
        this.firstDice = firstDice;
        this.secondDice = secondDice;
        this.pot = pot;
        this.rolledAmount = rolledAmount;
    }

    public int getFirstDice() {
        return firstDice;
    }

    public void setFirstDice(int firstDice) {
        this.firstDice = firstDice;
    }

    public int getSecondDice() {
        return secondDice;
    }

    public void setSecondDice(int secondDice) {
        this.secondDice = secondDice;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getRolledAmount() {
        return rolledAmount;
    }

    public void setRolledAmount(int rolledAmount) {
        this.rolledAmount = rolledAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanRollDiceTrickyOne that = (CanRollDiceTrickyOne) o;
        return playerIndex == that.playerIndex &&
                firstDice == that.firstDice &&
                secondDice == that.secondDice &&
                pot == that.pot &&
                rolledAmount == that.rolledAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex, firstDice, secondDice, pot, rolledAmount);
    }
}
