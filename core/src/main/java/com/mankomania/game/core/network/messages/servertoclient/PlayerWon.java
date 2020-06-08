package com.mankomania.game.core.network.messages.servertoclient;

/*
 Created by Fabian Oraze on 08.06.20
*/

import java.util.Objects;

public class PlayerWon {

    private int playerIndex;

    public PlayerWon() {
    }

    public PlayerWon(int playerIndex) {
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
        PlayerWon playerWon = (PlayerWon) o;
        return playerIndex == playerWon.playerIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }
}
