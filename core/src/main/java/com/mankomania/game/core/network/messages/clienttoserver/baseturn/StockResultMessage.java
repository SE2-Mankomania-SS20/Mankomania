package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

public class StockResultMessage {
    private int playerId;
    private int StockResult;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerIndex) {
        this.playerId = playerIndex;
    }

    public int getStockResult() {
        return StockResult;
    }

    public void setStockResult(int stockResult) {
        this.StockResult = stockResult;
    }

    public static StockResultMessage createStockResultMessage(int playerId, int StockResult) {
        StockResultMessage message = new StockResultMessage();
        message.setPlayerId(playerId);
        message.setStockResult(StockResult);

        return message;
    }
}
