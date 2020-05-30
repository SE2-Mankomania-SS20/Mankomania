package com.mankomania.game.core.network.messages.clienttoserver.trickyone;

import java.util.Objects;

/*********************************
 Created by Fabian Oraze on 30.05.20
 *********************************/

public class TrickyOneBaseMessage {


    protected int playerIndex;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrickyOneBaseMessage that = (TrickyOneBaseMessage) o;
        return getPlayerIndex() == that.getPlayerIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerIndex());
    }
}
