package com.mankomania.game.core.data;

import com.esotericsoftware.minlog.Log;
import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.player.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*
 Created by Fabian Oraze on 04.05.20
 */

/**
 * representation of the game/board
 */
public class GameData {
    private Field[] fields;
    private int[] startFieldsIndices;
    private int lotteryAmount;
    private Player localPlayer;
    private IDConverter converter;
    private boolean selectedOptional = false;

    // store this variables somewhere else, maybe in the player class itself?
    private int intersectionSelectionOption1 = -1;
    private int intersectionSelectionOption2 = -1;

    /**
     * array index of Player
     * Player Object that holds all player relevant info
     */
    private PlayerHashMap players;


    /**
     * HotelFieldIndex (Index from fields array)
     * PlayerID --> key from players HashMap
     */
    private HashMap<Integer, Integer> hotels;


    public GameData() {
        //Empty Constructor because Initialization of the date should be made later in gameLifeCycle
    }

    /**
     * Sets the local player. Needs only be called by the client, since the server has no "local player":
     * @param currentConnectionId the local connection id
     */
    public void setLocalPlayer(int currentConnectionId) {
        this.localPlayer = this.players.get(this.converter.getArrayIndexOfPlayer(currentConnectionId));
        Log.info("[initializePlayers] initalized players, local player = " + this.localPlayer.getOwnConnectionId() + ", local player field = " + this.localPlayer.getCurrentField());
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
            if (fields[i] instanceof HotelField) {
                hotels.put(i, null);
            }
        }
    }

    public Player getPlayerByConnectionId(int connectionId) {
        return this.players.get(this.converter.getArrayIndexOfPlayer(connectionId));
    }

    /**
     * Initializes player hashMap object with {@link IDConverter} parameter
     *
     * @param listIDs connection IDs which are gotten from server
     */
    public void intPlayers(ArrayList<Integer> listIDs) {
        converter = new IDConverter(listIDs);
        this.players = new PlayerHashMap();
        for (int i = 0; i < listIDs.size(); i++) {
            players.put(converter.getArrayIndices().get(i), new Player(this.startFieldsIndices[i], listIDs.get(i)));
            //set players start field to one of the 4 starting points beginning at index 78
            players.get(converter.getArrayIndices().get(i)).setFieldID(78 + i);
        }
        this.lotteryAmount = 0;
    }

    public Field getFieldById(int fieldId) {
        return this.fields[fieldId];
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public PlayerHashMap getPlayers() {
        return players;
    }

    /**
     * get start position for a certain player
     *
     * @param player defines which playerStart field will be used 1 to 4 possible
     * @return returns a Vector3 object which can be used with helper class to get Vector3
     */
    public Vector3 getStartPosition(int player) {
        if (player >= 0 && player < 4) {
            return fields[startFieldsIndices[player]].getPositions()[0];
        } else {
            return null;
        }
    }

    public void setPlayerToNewField(Integer connID, int field) {
        players.get(converter.getArrayIndexOfPlayer(connID)).setFieldID(field - 1);
    }

    public Vector3[] getFieldPos(int fieldID) {
        return fields[fieldID].getPositions();
    }

    public Vector3 getVector3FromField(int player) {
        int field = players.get(player).getFieldID();
        if (field >= 78) {
            return getStartPosition(player);
        } else {
            return fields[field].getPositions()[player];
        }
    }

    public Field getFieldByIndex(int fieldID) {
        return fields[fieldID];
    }

    public Field[] getFields() {
        return fields;
    }

    public void setLotteryAmount(int amount) {
        this.lotteryAmount = amount;
    }

    public int getLotteryAmount() {
        return this.lotteryAmount;
    }

    public void addFromLotteryAmountToPlayer(Integer connID) {
        int amount = this.lotteryAmount;
        this.lotteryAmount = 0;
        int playerId = converter.getArrayIndexOfPlayer(connID);
        players.get(playerId).addMoney(amount);
    }

    public void addToLotteryFromPlayer(Integer connID, int amountToPay) {
        this.lotteryAmount += amountToPay;
        int playerID = converter.getArrayIndexOfPlayer(connID);
        players.get(playerID).loseMoney(amountToPay);
    }

    public int getIntersectionSelectionOption1() {
        return intersectionSelectionOption1;
    }

    public void setIntersectionSelectionOption1(int intersectionSelectionOption1) {
        this.intersectionSelectionOption1 = intersectionSelectionOption1;
    }

    public int getIntersectionSelectionOption2() {
        return intersectionSelectionOption2;
    }

    public void setIntersectionSelectionOption2(int intersectionSelectionOption2) {
        this.intersectionSelectionOption2 = intersectionSelectionOption2;
    }

    public boolean isSelectedOptional() {
        return selectedOptional;
    }

    public void setSelectedOptional(boolean selectedOptional) {
        this.selectedOptional = selectedOptional;
    }
}
