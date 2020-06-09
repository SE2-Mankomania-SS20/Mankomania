package com.mankomania.game.core.network.messages.servertoclient.stock;

import java.util.Objects;

public class StartStockMessage {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartStockMessage that = (StartStockMessage) o;
        return playerIndex == that.playerIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }

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
