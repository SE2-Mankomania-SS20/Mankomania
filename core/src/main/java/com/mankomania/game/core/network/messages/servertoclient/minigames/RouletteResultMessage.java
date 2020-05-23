package com.mankomania.game.core.network.messages.servertoclient.minigames;

import java.util.HashMap;

public class RouletteResultMessage {
    private int playerID;
    private String result;
    private boolean winlost;
    private int winAmount;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isWinlost() {
        return winlost;
    }

    public void setWinlost(boolean winlost) {
        this.winlost = winlost;
    }

    public int getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(int winAmount) {
        this.winAmount = winAmount;
    }

    public RouletteResultMessage(int playerID, String result, boolean winlost, int winAmount) {
        this.playerID = playerID;
        this.result = result;
        this.winlost = winlost;
        this.winAmount = winAmount;
    }

    public RouletteResultMessage() {

    }
}
