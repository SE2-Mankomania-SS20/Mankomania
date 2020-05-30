package com.mankomania.game.core.network.messages.servertoclient.trickyone;

import com.mankomania.game.core.network.messages.clienttoserver.trickyone.TrickyOneBaseMessage;

import java.util.Objects;

/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

public class StartTrickyOne extends TrickyOneBaseMessage {

    public StartTrickyOne() {
    }

    public StartTrickyOne(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
