package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class ServerData {

    private final int MAX_PLAYERS = 4;

    private ArrayList<Integer> listID;

    private boolean gameOpen;

    /**
     * @param Connection holds the player connection
     * @param Boolean indicates whether the player is ready to play
     */
    private HashMap<Connection, Boolean> players;

    public ServerData() {
        this.players = new HashMap<>();
        this.listID = new ArrayList<>();
        gameOpen = true;
    }

    public boolean connectPlayer(Connection con) {
        if (!gameOpen) {
            return false;
        } else if (players.size() <= MAX_PLAYERS) {
            players.put(con, false);
            listID.add(con.getID());
            if (listID.size() == MAX_PLAYERS) {
                gameOpen = false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void disconnectPlayer(Connection con) {
        players.remove(con);
        listID.remove((Integer) con.getID());
        if (players.size() == 0) {
            gameOpen = true;
        }
    }

    public void playerReady(Connection con, boolean ready) {
        players.put(con, ready);
    }

    public boolean checkForStart() {

        if (players.size() > 1 && !(players.containsValue(false))) {
            gameOpen = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean getCurrentGameOpen() {
        return gameOpen;
    }

    public ArrayList<Integer> initPlayerList() {
        return listID;
    }
}
