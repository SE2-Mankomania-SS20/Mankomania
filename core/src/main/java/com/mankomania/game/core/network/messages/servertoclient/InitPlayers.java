package com.mankomania.game.core.network.messages.servertoclient;

import java.util.List;

/*
 Created by Fabian Oraze on 04.05.20
 */

public class InitPlayers {

    public List<Integer> playerIDs;

    public InitPlayers(List<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public InitPlayers() {
        // empty for Kryonet
    }
}
