package com.mankomania.game.core.data;

import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.player.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 04.05.20
 *********************************/

public class GameData {
    private Field[] fields;
    private int[] startFieldsIndices;
    private int lotteryAmount;
    private Player localPlayer;

    /**
     * key: connection ID from Player
     * value: Player Object that holds all player relevant info
     */
    private PlayerHashMap players;


    /**
     * key: HotelFieldIndex (Index from fields array)
     * value: PlayerID --> key from players HashMap
     */
    private HashMap<Integer, Integer> hotels;

    public GameData() {
    }

    /**
     * Initializes player hashMap object with given parameter. Has to be called AFTER loadData!
     *
     * @param listIDs connection IDs which are gotten from server
     */
    public void initializePlayers(ArrayList<Integer> listIDs){
        this.players = new PlayerHashMap();

        // create each player, setting the start positions
        for (int i = 0; i < listIDs.size(); i++) {
            this.players.put(listIDs.get(i), new Player(this.startFieldsIndices[i], listIDs.get(i)));
        }

        this.lotteryAmount = 0;
    }

    /**
     * Sets the local player. Needs only be called by the client, since the server has no "local player":
     * @param currentConnectionId the local connection id
     */
    public void setLocalPlayer(int currentConnectionId) {
        this.localPlayer = this.players.get(currentConnectionId);
        System.out.println("[initializePlayers] initalized players, local player = " + this.localPlayer.getOwnConnectionId() + ", local player field = " + this.localPlayer.getCurrentField());
    }

    /**
     * Method to load initial data into gameData object
     *
     * @param stream gets data from path gotten by stream
     */
    public void loadData(InputStream stream) {
        FieldDataLoader loader = new FieldDataLoader();
        loader.loadJson(stream);
        fields = loader.parseFields();
        startFieldsIndices = loader.getStartFieldIndex();
        hotels = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof HotelField){
                hotels.put(i, null);
            }
        }
    }

    public Player getPlayerByConnectionId(int connectionId) {
        return this.players.get(connectionId);
    }

    public Field getFieldById(int fieldId) {
        return this.fields[fieldId];
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }
}
