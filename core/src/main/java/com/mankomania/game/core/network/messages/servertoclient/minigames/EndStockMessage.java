package com.mankomania.game.core.network.messages.servertoclient.minigames;

import java.util.Map;

public class EndStockMessage {
    //key: connectionID, value: Profit
    private Map<Integer,Integer> playerProfit;


    public Map<Integer, Integer> getPlayerProfit() {
        return playerProfit;
    }

    public void setPlayerProfit(Map<Integer, Integer> playerProfit) {
        this.playerProfit = playerProfit;
    }
}
