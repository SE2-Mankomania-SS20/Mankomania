package com.mankomania.game.core.network.messages.servertoclient;

import java.util.ArrayList;

/*********************************
 Created by Fabian Oraze on 04.05.20
 *********************************/

public class InitPlayers {

    public ArrayList<Integer> playerIDs;

    public InitPlayers(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public InitPlayers() {
        // empty for Kryonet
    }
}
