package com.mankomania.game.core.network.messages.clienttoserver.minigames;

public class RouletteStakeMessage {
    //message for server
    private int playerId;
    private int amountBet;
    private int selectedBet;

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

    public int getSelectedBet() {
        return selectedBet;
    }

    public void setSelectedBet(int selectedBet) {
        this.selectedBet = selectedBet;
    }

    public RouletteStakeMessage(int playerId, int amountBet, int selectedBet) {
        this.playerId = playerId;
        this.amountBet = amountBet;
        this.selectedBet = selectedBet;
    }

    public RouletteStakeMessage() {

    }
}
