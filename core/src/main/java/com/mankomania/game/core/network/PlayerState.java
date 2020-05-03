package com.mankomania.game.core.network;

import com.mankomania.game.core.player.Player;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class PlayerState {

    private boolean ready;
    private Player player;

    public PlayerState(Player player) {
        this.player = player;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean getReady() {
        return ready;
    }


}
