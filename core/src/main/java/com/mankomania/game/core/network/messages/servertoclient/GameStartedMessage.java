package com.mankomania.game.core.network.messages.servertoclient;

import java.util.ArrayList;

/**
 * Signalised all players that the game has started.
 */
public class GameStartedMessage {
    private ArrayList<Integer> playerIds;

    public ArrayList<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(ArrayList<Integer> playerIds) {
        this.playerIds = playerIds;
    }
}
