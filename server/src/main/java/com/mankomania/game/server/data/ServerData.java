package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.server.game.GameStateLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 Created by Fabian Oraze on 03.05.20
 */

public class ServerData {

    /**
     * max players allowed in the game (should be 4 all the time since board and the rest of the game is designed for only 4 )
     */
    private final int MAX_PLAYERS = 4;
    /**
     * min players required to start a game (more can join and click ready)
     */
    private final int MIN_PLAYERS = 1;

    // listID holds the connection id's of the players (0 -> connection if of first player, 1 -> ..., etc) (!)
    private final ArrayList<Integer> listID;
    private final LinkedHashMap<Integer, Connection> userMap; // maps connection id (= player id) to the corresponding Connection
    private int currentPlayerTurn = 0; // in 0-3, so listID[currentPlayerTurn] gives the current player connection id
    private int movesLeftAfterIntersection = -1; // stores the fields left to move after a player reaches an intersection, which needs a decision from the player

    private boolean gameOpen;

    private final GameData gameData;
    private final GameStateLogic gameStateLogic;

    /**
     * Connection holds the player connection
     * Boolean indicates whether the player is ready to play
     */
    private final HashMap<Connection, Boolean> playersReady;

    public ServerData(Server server) {
        this.playersReady = new HashMap<>();
        this.listID = new ArrayList<>();
        this.userMap = new LinkedHashMap<>();
        gameData = new GameData();
        gameStateLogic = new GameStateLogic(this, server);

        gameOpen = true;
    }

    public GameStateLogic getGameStateLogic() {
        return gameStateLogic;
    }

    public GameData getGameData() {
        return gameData;
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

    public void playerReady(Connection con) {
        playersReady.put(con, true);
    }

    public boolean checkForStart() {
        // TODO: change minimum player size
        if (playersReady.size() >= MIN_PLAYERS && !(playersReady.containsValue(false))) {
            gameOpen = false;
            this.currentPlayerTurn = 0; // reset the current player turn
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

    public Map<Integer, Connection> getUserMap() {
        return userMap;
    }

    public int getTotalPlayerSize() {
        return this.userMap.size();
    }

    /**
     * Gets the connection id of the player whos turn it is currently.
     *
     * @return the connection id of said player
     */
    public int getCurrentPlayerTurnConnectionId() {
        return this.listID.get(this.currentPlayerTurn);
    }

    /**
     * Gets the player whos turn it is currently. Care: does not return the player id (connection id),
     * but a number between 0 and 3, referring to the index of the playerIds array.
     *
     * @return index of the players whos turn it is
     */
    public int getCurrentPlayerTurn() {
        return this.currentPlayerTurn;
    }

    /**
     * Sets the player who is currently on turn to the next player.
     *
     * @return the new player id
     */
    public int setNextPlayerTurn() {
        this.currentPlayerTurn = (this.currentPlayerTurn + 1) % this.getTotalPlayerSize();
        return this.currentPlayerTurn;
    }

    /**
     * Gets the amount of fields left to move after a player chose an intersection path.
     *
     * @return the amount of fields left to move
     */
    public int getMovesLeftAfterIntersection() {
        return movesLeftAfterIntersection;
    }

    /**
     * Gets the amount of fields left to move after a player chose an intersection path.
     *
     * @param movesLeftAfterIntersection the amount of fields left to move
     */
    public void setMovesLeftAfterIntersection(int movesLeftAfterIntersection) {
        this.movesLeftAfterIntersection = movesLeftAfterIntersection;
    }
}
