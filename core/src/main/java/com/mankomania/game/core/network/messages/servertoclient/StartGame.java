package com.mankomania.game.core.network.messages.servertoclient;

import com.mankomania.game.core.player.Player;

import java.util.List;

/**
 * Signalised all players that the game has started.
 */
public class StartGame {
    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
