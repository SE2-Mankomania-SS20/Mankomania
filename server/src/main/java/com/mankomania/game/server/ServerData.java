package com.mankomania.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.mankomania.game.core.player.Player;

import java.util.ArrayList;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class ServerData {

    private final int MAX_PLAYERS = 4;
    private ArrayList<Connection> players;


    public ServerData() {
        this.players = new ArrayList<>();
    }

    public boolean connectPlayer(Connection con) {
        if (players.size() <= MAX_PLAYERS) {
            players.add(con);
            return true;
        } else {
            return false;
        }
    }

    public void disconnectPlayer(Connection con) {
        players.remove(con);
    }


}
