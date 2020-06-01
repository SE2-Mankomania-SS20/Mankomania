package com.mankomania.game.core.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.fields.types.StartField;
import com.mankomania.game.core.player.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * container for needed variables that should be displayed on screen related to miniGamTrickyOne
     */
    private TrickyOneData trickyOneData;

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

    /**
     * playerIndex from players array in gamedata tha is currently at turn
     */
    private int currentPlayerTurn;


    public GameData() {
        players = new ArrayList<>();
        trickyOneData = new TrickyOneData();
        lotteryAmount = 0;
        currentPlayerTurn = 0;
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

    /**
     * Update the GameData without overriding object references
     *
     * @param gameUpdate {@link GameUpdate}
     */
    public void updateGameData(GameUpdate gameUpdate) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).update(gameUpdate.getPlayers().get(i));
        }
        currentPlayerTurn = gameUpdate.getCurrentPlayerTurn();
        hotels.clear();
        hotels.putAll(gameUpdate.getHotels());
        lotteryAmount = gameUpdate.getLotteryAmount();
    }

    public int getCurrentPlayerTurnIndex() {
        return currentPlayerTurn;
    }

    public Field getCurrentPlayerTurnField() {
        return fields[players.get(currentPlayerTurn).getCurrentFieldIndex()];
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public void setNextPlayerTurn() {
        currentPlayerTurn = (currentPlayerTurn + 1) % players.size();
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

    public Player getCurrentPlayer() {
        return players.get(currentPlayerTurn);
    }

    public boolean isCurrentPlayerMovePathEmpty() {
        return players.get(currentPlayerTurn).isMovePathEmpty();
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
        return players.get(playerIndex).getPosition();
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

    /**
     * Win the lottery if there is money to win and reset lotteryAmount
     * else you pay 50000
     *
     * @param playerIndex index of player of players in {@link GameData}
     */
    public int winLottery(int playerIndex) {
        if (lotteryAmount == 0) {
            players.get(playerIndex).loseMoney(50000);
            return -50000;
        } else {
            int win = lotteryAmount;
            players.get(playerIndex).addMoney(lotteryAmount);
            lotteryAmount = 0;
            return win;
        }
    }

    /**
     * Buy a lotteryticket for given playerIndex and add price to lotteryAmount (win amount)
     *
     * @param playerIndex index of player of players in {@link GameData}
     * @param price       for the lottery ticket
     */
    public void buyLotteryTickets(int playerIndex, int price) {
        players.get(playerIndex).loseMoney(price);
        lotteryAmount += price;
    }

    /**
     * @return returns updated currentPlayerTurn position
     */
    public Vector3 moveCurrentPlayer() {
        Player player = getPlayers().get(currentPlayerTurn);
        int nextFieldIndex = player.popFromMovePath();
        player.updateField(fields[nextFieldIndex]);
        return player.getPosition();
    }

    /**
     * @return returns updated currentPlayerTurn position
     */
    public Vector3 movePlayer(int playerIndex) {
        Player player = getPlayers().get(playerIndex);
        int nextFieldIndex = player.popFromMovePath();
        player.updateField(fields[nextFieldIndex]);
        return player.getPosition();
    }

    public TrickyOneData getTrickyOneData() {
        return trickyOneData;
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

    // merge: removable?
    public Map<Hotel, Integer> getHotels() {
        return hotels;
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

    public void resetHotels() {
        // stored hotels will be moved to players so this reest will not be neccessary anymore
        for (HotelField field : this.hotelFields) {
            field.setOwnerPlayerIndex(-1);
        }
    }
}
