package com.mankomania.game.core.network.messages.servertoclient.roulette;

public class RouletteResultMessage {
    private int playerIndex;
    private String resultOfRouletteWheel;
    private boolean winOrLost;
    private int amountWin;
    private int bet;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
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

    public RouletteResultMessage(int playerIndex, String resultOfRouletteWheel, boolean winOrLost, int amountWin) {
        this.playerIndex = playerIndex;
        this.resultOfRouletteWheel = resultOfRouletteWheel;
        this.winOrLost = winOrLost;
        this.amountWin = amountWin;
    }

    public RouletteResultMessage() {

    }
}
