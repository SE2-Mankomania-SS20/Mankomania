package com.mankomania.game.core.data;

/*
 Created by Fabian Oraze on 23.05.20
*/

/*used as a container for TrickyOne miniGame to allow players
 to get info for their render methods in the screen*/

public class TrickyOneData {

    private int pot;
    private int firstDice;
    private int secondDice;
    private int rolledAmount;
    private boolean isInputEnabled;
    private boolean gotUpdate;

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
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

    public int getRolledAmount() {
        return rolledAmount;
    }

    public void setRolledAmount(int rolledAmount) {
        this.rolledAmount = rolledAmount;
    }

    public boolean isInputEnabled() {
        return isInputEnabled;
    }

    public void setInputEnabled(boolean inputEnabled) {
        this.isInputEnabled = inputEnabled;
    }

    public boolean isGotUpdate() {
        return gotUpdate;
    }

    public void setGotUpdate(boolean gotUpdate) {
        this.gotUpdate = gotUpdate;
    }
}
