package com.mankomania.game.core.network.messages.servertoclient.stock;

import com.mankomania.game.core.player.Stock;

public class EndStockMessage {
    private Stock stock;
    private boolean isRising;

    public EndStockMessage() {
        // empty for kryonet
    }

    public EndStockMessage(Stock stock) {
        this.stock = stock;
    }

    public boolean isRising() {
        return isRising;
    }

    public void setRising(boolean rising) {
        isRising = rising;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
