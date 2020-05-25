package com.mankomania.game.core.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.player.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 Created by Fabian Oraze on 04.05.20
 */

/**
 * representation of the game/board
 */
public class GameData {
    /**
     * all fields on the board {@link Field}
     */
    private Field[] fields;

    /**
     * indices of the startFields from fields array
     */
    private int[] startFieldsIndices;

    /**
     * current lottery amount
     */
    private int lotteryAmount;

    // store this variables somewhere else, maybe in the player class itself?
    private int intersectionSelectionOption1 = -1;
    private int intersectionSelectionOption2 = -1;

    /**
     * array  of Players
     * Player Object that holds all player relevant info
     */
    private List<Player> players;

    /**
     * HotelFieldIndex (Index from fields array)
     * PlayerID --> key from players HashMap
     */
    private HashMap<Hotel, Integer> hotels;


    public GameData() {
        players = new ArrayList<>();
        lotteryAmount = 0;
        loadData(GameData.class.getResourceAsStream("/resources/data.json"));
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
        for (Field field : fields) {
            if (field instanceof HotelField) {
                hotels.put(((HotelField) field).getHotelType(), null);
            }
        }
    }

    public int[] getStartFieldsIndices() {
        return startFieldsIndices;
    }

    public Player getPlayerByConnectionId(int connectionId) {
        for (Player player : players) {
            if (player.getConnectionId() == connectionId)
                return player;
        }
        return null;
    }

    /**
     * Initializes players
     *
     * @param players list of players
     */
    public void intPlayers(List<Player> players) {
        this.players = players;
        lotteryAmount = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * get start position for a certain player
     *
     * @param playerIndex defines which playerStart field will be used 1 to 4 possible
     * @return returns a Vector3 object which can be used with helper class to get Vector3
     */
    public Vector3 getStartPosition(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 4) {
            return fields[startFieldsIndices[playerIndex]].getPositions()[0];
        } else {
            return null;
        }
    }

    /**
     * set the targetfield, on the client the current field will be updated every 1 sec until player reaches the targetfield
     * @param playerIndex player to set the targetField on
     * @param field field the player will be moving to
     */
    public void setPlayerToField(int playerIndex, int field) {
        players.get(playerIndex).setTargetFieldIndex(fields[field]);
    }

    /**
     * @param fieldIndex index to field from fields array in {@link GameData}
     * @return position of specified field
     */
    public Vector3[] getFieldPos(int fieldIndex) {
        return fields[fieldIndex].getPositions();
    }

    /**
     * @param playerIndex index of player of players in {@link GameData}
     * @return postion of specified playerIndex
     */
    public Vector3 getPlayerPosition(int playerIndex) {
        return  players.get(playerIndex).getPosition();
    }

    /**
     * @param fieldIndex index to field from fields array in {@link GameData}
     * @return Field from fields array in {@link GameData}
     */
    public Field getFieldByIndex(int fieldIndex) {
        return fields[fieldIndex];
    }

    public Field[] getFields() {
        return fields;
    }

    public void setLotteryAmount(int amount) {
        lotteryAmount = amount;
    }

    public int getLotteryAmount() {
        return lotteryAmount;
    }

    public void winLottery(int playerIndex) {
        players.get(playerIndex).addMoney(lotteryAmount);
        lotteryAmount = 0;
    }

    public void buyLotteryTickets(int playerIndex, int price) {
        players.get(playerIndex).loseMoney(price);
        lotteryAmount += price;
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

    /**
     * @param playerIndex playerIndex from players array in {@link GameData}
     * @return color for given player
     */
    public Color getColorOfPlayer(int playerIndex) {
        switch (playerIndex) {
            case 0: {
                return Color.BLUE;
            }
            case 1: {
                return Color.GREEN;
            }
            case 2: {
                return Color.RED;
            }
            case 3: {
                return Color.YELLOW;
            }
            default: {
                return Color.BLACK;
            }
        }
    }
}
