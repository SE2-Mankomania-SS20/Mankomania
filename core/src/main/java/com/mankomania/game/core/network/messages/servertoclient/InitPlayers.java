package com.mankomania.game.core.network.messages.servertoclient;

import java.util.ArrayList;
import java.util.List;

/*
 Created by Fabian Oraze on 04.05.20
 */

public class InitPlayers {

    public ArrayList<Integer> playerIDs;

    public InitPlayers(List<Integer> playerIDs) {
        this.playerIDs = (ArrayList<Integer>) playerIDs;
    }

    public InitPlayers() {
        // empty for Kryonet
    }
}
