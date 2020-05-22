package com.mankomania.game.core.network.messages.servertoclient.minigames;

import java.util.HashMap;

public class EndStockMessage {
    //key: connectionID, value: Profit
    private HashMap<Integer,Integer> playerProfit;


    public HashMap<Integer, Integer> getPlayerProfit() {
        return playerProfit;
    }

    public void setPlayerProfit(HashMap<Integer, Integer> playerProfit) {
        this.playerProfit = playerProfit;
    }
}
