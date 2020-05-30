package com.mankomania.game.core.network.messages.clienttoserver.stock;

public class StockResultMessage {
    private int playerId;
    private int stockResult;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerIndex) {
        this.playerId = playerIndex;
    }

    public int getStockResult() {
        return stockResult;
    }

    public void setStockResult(int stockResult) {
        this.stockResult = stockResult;
    }

    public static StockResultMessage createStockResultMessage(int playerId, int stockResult) {
        StockResultMessage message = new StockResultMessage();
        message.setPlayerId(playerId);
        message.setStockResult(stockResult);

        return message;
    }
}
