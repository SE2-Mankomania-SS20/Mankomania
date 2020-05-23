package com.mankomania.game.core.network.messages.clienttoserver.minigames;

public class RouletteStakeMessage {
    //message for server
    private int playerId;
    private int amountBet;
    private String bet;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAmountBet() {
        return amountBet;
    }

    public void setAmountBet(int amountBet) {
        this.amountBet = amountBet;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public RouletteStakeMessage(int playerId, int amountBet, String bet) {
        this.playerId = playerId;
        this.amountBet = amountBet;
        this.bet = bet;
    }

    public RouletteStakeMessage() {

    }
}
