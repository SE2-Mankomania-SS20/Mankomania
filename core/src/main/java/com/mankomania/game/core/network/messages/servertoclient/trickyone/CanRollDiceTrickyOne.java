package com.mankomania.game.core.network.messages.servertoclient.trickyone;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class CanRollDiceTrickyOne {

    private int playerIndex;
    private int firstDice;
    private int secondDice;
    private int pot;

    public CanRollDiceTrickyOne() {
    }

    public CanRollDiceTrickyOne(int playerIndex, int firstDice, int secondDice, int pot) {
        this.playerIndex = playerIndex;
        this.firstDice = firstDice;
        this.secondDice = secondDice;
        this.pot = pot;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
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
}
