package com.mankomania.game.core.network.messages.servertoclient;

import java.util.List;

/**
 * Signalised all players that the game has started.
 */
public class StartGame {
    private List<Integer> playerIds;

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }
}
