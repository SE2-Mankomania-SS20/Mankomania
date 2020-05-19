package com.mankomania.game.core.network.messages;

/*
 Created by Fabian Oraze on 03.05.20
 */

public class PlayerGameReady {

    public boolean playerReady;
    public boolean gameReady;

    public PlayerGameReady(boolean playerReady, boolean gameReady) {
        this.playerReady = playerReady;
        this.gameReady = gameReady;
    }

    public PlayerGameReady() {
        // empty for Kryonet
    }
}
