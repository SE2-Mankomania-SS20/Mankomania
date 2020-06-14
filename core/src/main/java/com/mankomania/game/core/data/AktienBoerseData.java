package com.mankomania.game.core.data;

import com.mankomania.game.core.player.Stock;

public class AktienBoerseData {
    private Stock stock;
    private boolean isRising;
    private boolean needUpdate;

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

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
