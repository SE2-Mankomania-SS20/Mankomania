package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class ServerData {

    private final int MAX_PLAYERS = 4;

    private ArrayList<Integer> listID;
    private LinkedHashMap<Integer, Connection> userMap; // maps connection id (= player id) to the corresponding Connection
    private int currentPlayer;

    private boolean gameOpen;

    /**
     * @param Connection holds the player connection
     * @param Boolean indicates whether the player is ready to play
     */
    private HashMap<Connection, Boolean> playersReady;

    public ServerData() {
        this.playersReady = new HashMap<>();
        this.listID = new ArrayList<>();
        this.userMap = new LinkedHashMap<>();

        gameOpen = true;
    }

    public boolean connectPlayer(Connection con) {
        if (!gameOpen) {
            return false;
        } else if (playersReady.size() <= MAX_PLAYERS) {
            playersReady.put(con, false);
            listID.add(con.getID());
            if (listID.size() == MAX_PLAYERS) {
                gameOpen = false;
            }

            this.userMap.put(con.getID(), con);

            return true;
        } else {
            return false;
        }
    }

    public void disconnectPlayer(Connection con) {
        playersReady.remove(con);
        listID.remove((Integer) con.getID());
        if (playersReady.size() == 0) {
            gameOpen = true;
        }
        this.userMap.remove(con.getID());
    }

    public void playerReady(Connection con, boolean ready) {
        playersReady.put(con, ready);
    }

    public boolean checkForStart() {
        // TODO: change minimum player size
        if (playersReady.size() >= 1 && !(playersReady.containsValue(false))) {
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

    public LinkedHashMap<Integer, Connection> getUserMap() {
        return userMap;
    }
}
