package com.mankomania.game.core.network.messages.servertoclient.minigames;

public class RouletteResultMessage {
    private int playerID;
    private String resultOfRouletteWheel;
    private boolean winOrLost;
    private int amountWin;
    private int bet;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getResultOfRouletteWheel() {
        return resultOfRouletteWheel;
    }

    public void setResultOfRouletteWheel(String resultOfRouletteWheel) {
        this.resultOfRouletteWheel = resultOfRouletteWheel;
    }

    public boolean isWinOrLost() {
        return winOrLost;
    }

    public void setWinOrLost(boolean winOrLost) {
        this.winOrLost = winOrLost;
    }

    public int getAmountWin() {
        return amountWin;
    }

    public void setAmountWin(int amountWin) {
        this.amountWin = amountWin;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public RouletteResultMessage(int playerID, String resultOfRouletteWheel, boolean winOrLost, int amountWin) {
        this.playerID = playerID;
        this.resultOfRouletteWheel = resultOfRouletteWheel;
        this.winOrLost = winOrLost;
        this.amountWin = amountWin;
    }

    public RouletteResultMessage() {

    }
}
