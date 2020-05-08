package com.mankomania.game.core.network.messages.servertoclient;

import java.io.Serializable;
import java.util.ArrayList;

/*********************************
 Created by Fabian Oraze on 04.05.20
 *********************************/

public class InitPlayers {

    public ArrayList<Integer> playerIDs;

    public InitPlayers() {
    }

    public InitPlayers(ArrayList<Integer> ids) {
        this.playerIDs = ids;
    }

}
