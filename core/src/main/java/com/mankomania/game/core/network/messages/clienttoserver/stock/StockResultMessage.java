package com.mankomania.game.core.network.messages.clienttoserver.stock;

public class StockResultMessage {

public StockResultMessage(){
    //empty for SE2
}
    public StockResultMessage(int playerIndex, int stockResult) {
        this.playerIndex = playerIndex;
        this.stockResult = stockResult;
    }

    private int playerIndex;
    private int stockResult;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getStockResult() {
        return stockResult;
    }

    public void setStockResult(int stockResult) {
        this.stockResult = stockResult;
    }

}
