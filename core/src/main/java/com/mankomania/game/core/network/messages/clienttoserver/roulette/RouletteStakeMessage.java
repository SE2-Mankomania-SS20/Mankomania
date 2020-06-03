package com.mankomania.game.core.network.messages.clienttoserver.roulette;

public class RouletteStakeMessage {
    //message for server
    private int rsmPlayerIndex;
    private int rsmAmountBet;
    private int rsmSelectedBet;

    public int getRsmPlayerIndex() {
        return rsmPlayerIndex;
    }

    public void setRsmPlayerIndex(int rsmPlayerIndex) {
        this.rsmPlayerIndex = rsmPlayerIndex;
    }

    public int getRsmAmountBet() {
        return rsmAmountBet;
    }

    public void setRsmAmountBet(int rsmAmountBet) {
        this.rsmAmountBet = rsmAmountBet;
    }

    public int getRsmSelectedBet() {
        return rsmSelectedBet;
    }

    public void setRsmSelectedBet(int rsmSelectedBet) {
        this.rsmSelectedBet = rsmSelectedBet;
    }

    public RouletteStakeMessage(int rsmPlayerIndex, int rsmAmountBet, int rsmSelectedBet) {
        this.rsmPlayerIndex = rsmPlayerIndex;
        this.rsmAmountBet = rsmAmountBet;
        this.rsmSelectedBet = rsmSelectedBet;
    }

    public RouletteStakeMessage() {

    }
}
