package com.mankomania.game.core.network.messages.servertoclient.trickyone;

import java.util.Objects;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class StartTrickyOne {
    private int playerIndex;

    public StartTrickyOne() {
    }

    public StartTrickyOne(int playerIndex) {
        this.playerIndex = playerIndex;
    }

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
        StartTrickyOne that = (StartTrickyOne) o;
        return playerIndex == that.playerIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }
}
