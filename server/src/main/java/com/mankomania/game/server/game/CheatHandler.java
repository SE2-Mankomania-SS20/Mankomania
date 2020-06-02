package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 02.06.20
 */

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.server.data.ServerData;

/**
 * Handler class to process cheating actions
 */
public class CheatHandler {

    private ServerData refServerData;
    private Server refServer;

    /**
     * should be called in {@link ServerData} to initialize handler
     *
     * @param refServer     reference Object to Kryo Server
     * @param refServerData reference Object to ServerData which holds all data related to {@link GameData}
     */
    public CheatHandler(Server refServer, ServerData refServerData) {
        this.refServerData = refServerData;
        this.refServer = refServer;
    }




}
