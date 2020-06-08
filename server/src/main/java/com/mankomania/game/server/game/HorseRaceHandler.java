package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.server.data.ServerData;

public class HorseRaceHandler {
    //necessary references
    private final Server refServer;
    private final ServerData refServerData;

    public HorseRaceHandler(Server refServer, ServerData refServerData) {
        this.refServer = refServer;
        this.refServerData = refServerData;
    }

    public void start(){
        refServer.sendToAllTCP(new HorseRaceStart());
    }

    public void end(){
        refServerData.movePlayer(false,false);
    }
}
