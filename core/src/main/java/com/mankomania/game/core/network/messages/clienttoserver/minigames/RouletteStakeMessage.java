package com.mankomania.game.core.network.messages.clienttoserver.minigames;

public class RouletteStakeMessage {
    //message for server
    private int rsmPlayerId;
    private int rsmAmountBet;
    private int rsmSelectedBet;

    public int getRsmPlayerId() {
        return rsmPlayerId;
    }

    public void setRsmPlayerId(int rsmPlayerId) {
        this.rsmPlayerId = rsmPlayerId;
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

    public RouletteStakeMessage(int rsmPlayerId, int rsmAmountBet, int rsmSelectedBet) {
        this.rsmPlayerId = rsmPlayerId;
        this.rsmAmountBet = rsmAmountBet;
        this.rsmSelectedBet = rsmSelectedBet;
    }

    public RouletteStakeMessage() {

    }
}
