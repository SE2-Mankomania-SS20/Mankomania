package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;
import com.mankomania.game.server.game.GameStateLogic;

import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class ServerData {

    private final int MAX_PLAYERS = 4;

    private GameState gameState;

    private ArrayList<Integer> listID;


    /**
     * @param Connection holds the player connection
     * @param Boolean indicates whether the player is ready to play
     */
    private HashMap<Connection, Boolean> players;


    public ServerData() {
        this.players = new HashMap<>();
        this.listID = new ArrayList<>();
        gameState = GameState.START;
    }

    public boolean connectPlayer(Connection con) {
        if (gameState == GameState.GAME_LOOP) {
            return false;
        } else if (players.size() <= MAX_PLAYERS) {
            players.put(con, false);
            listID.add(con.getID());
            gameState = GameState.LOBBY;
            return true;
        } else {
            return false;
        }
    }

    public void disconnectPlayer(Connection con) {
        players.remove(con);
        listID.remove(con.getID());
        if (players.size() == 0) {
            gameState = GameState.START;
        }
    }

    public void playerReady(Connection con, boolean ready) {
        players.put(con, ready);
    }

    public boolean checkForStart() {

        if (players.size() > 1 && !(players.containsValue(false))) {
            gameState = GameState.GAME_LOOP;
            return true;
        } else {
            return false;
        }
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public ArrayList<Integer> initPlayerList() {
        return listID;
    }
}
