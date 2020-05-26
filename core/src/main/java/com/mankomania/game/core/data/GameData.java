package com.mankomania.game.core.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.fields.types.StartField;
import com.mankomania.game.core.player.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private boolean selectedOptional = false;

    // store this variables somewhere else, maybe in the player class itself?
    private int intersectionSelectionOption1 = -1;
    private int intersectionSelectionOption2 = -1;

    // store which hotel field the player is allowed to buy currently after getting a PlayerCanBuyHotel message
    private int buyableHotelFieldId = -1;

    /**
     * array  of Players
     * Player Object that holds all player relevant info
     * is indexed with LOCAL ID, NOT CONNECTION ID (!)
     */
    private List<Player> players;

    /**
     * only holds references to all the hotel fields for faster access (otherwhise we have to iterate over all the fields to find the hotelfields)
     */
    private HotelField[] hotelFields;


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
        hotelFields = new HotelField[6];
        int hotelFieldIndexCounter = 0;
        for (Field field : fields) {
            if (field instanceof HotelField) {
                hotelFields[hotelFieldIndexCounter++] = (HotelField) field;
            }
        }
    }

    // TODO: obsolete since a field now stores it's own id as property?
    public int getFieldIndex(Field field) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == field) {
                return i;
            }
        }
        return -1;
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

    public void setPlayerToField(int playerIndex, int field) {
        players.get(playerIndex).setTargetFieldIndex(fields[field]);
    }

    public Vector3[] getFieldPos(int fieldID) {
        return fields[fieldID].getPositions();
    }

    public Vector3 getPlayerPosition(int player) {
        Field field = fields[players.get(player).getCurrentField()];
        if (field instanceof StartField) {
            return getStartPosition(player);
        } else {
            return field.getPositions()[player];
        }
    }

    public Field getFieldByIndex(int fieldID) {
        return fields[fieldID];
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

    public void addFromLotteryAmountToPlayer(Integer connID) {
        getPlayerByConnectionId(connID).addMoney(lotteryAmount);
        lotteryAmount = 0;
    }

    public void addToLotteryFromPlayer(Integer connID, int amountToPay) {
        lotteryAmount += amountToPay;
        Player player = getPlayerByConnectionId(connID);
        player.loseMoney(amountToPay);
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


    /* ======== HOTELS ======== */

    /**
     * Gets the player that currently owns the hotel with given field id.
     *
     * @param hotelFieldId the field id of the hotel that owner should be returned
     * @return the player that owns the hotel or null if there is no owner (or the field is not even a hotel field)
     */
    public Player getOwnerOfHotel(int hotelFieldId) {
        Field field = this.getFieldByIndex(hotelFieldId);
        if (field instanceof HotelField) {
            int ownerPlayerIndex = ((HotelField) field).getOwnerPlayerIndex();
            if (ownerPlayerIndex >= 0 && ownerPlayerIndex < this.players.size()) {
                return this.players.get(ownerPlayerIndex);
            }
        }
        return null;
    }

    /**
     * Returns the hotel field that's owned by a player with given player index.
     *
     * @param playerIndex the player's index
     * @return the hotel field that the given player owns or null if he does not own a hotel
     */
    public HotelField getHotelOwnedByPlayer(int playerIndex) {
        // iterate over the hotelfields to look for a field with given player index as owner
        for (HotelField hotelField : this.hotelFields) {
            if (hotelField.getOwnerPlayerIndex() == playerIndex) {
                return hotelField;
            }
        }
        return null;
    }

    public HotelField[] getHotelFields() {
        return hotelFields;
    }

    public int getBuyableHotelFieldId() {
        return buyableHotelFieldId;
    }

    public void setBuyableHotelFieldId(int buyableHotelFieldId) {
        this.buyableHotelFieldId = buyableHotelFieldId;
    }
}
