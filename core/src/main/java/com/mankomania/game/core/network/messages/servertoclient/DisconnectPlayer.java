package com.mankomania.game.core.network.messages.servertoclient;

/*********************************
 Created by Fabian Oraze on 09.05.20
 *********************************/

public class DisconnectPlayer {

    public String errTxt;

    public DisconnectPlayer() {
        // empty for Kryonet
    }

    public DisconnectPlayer(String errTxt) {
        this.errTxt = errTxt;
    }
}
