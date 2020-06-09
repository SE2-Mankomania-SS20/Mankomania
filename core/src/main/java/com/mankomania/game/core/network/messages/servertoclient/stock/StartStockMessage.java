package com.mankomania.game.core.network.messages.servertoclient.stock;

public class StartStockMessage {
    protected int playerIndex;
    public  StartStockMessage(){
    }
    public StartStockMessage(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
