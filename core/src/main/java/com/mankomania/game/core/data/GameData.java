package com.mankomania.game.core.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
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

    /**
     * playerIndex from players array in gamedata tha is currently at turn
     */
    private int currentPlayerTurn;


    public GameData() {
        players = new ArrayList<>();
        lotteryAmount = 0;
        currentPlayerTurn = -1;
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

    public Player getCurrentPlayer(){
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
     * Win the lottery and reset lotteryAmount
     *
     * @param playerIndex index of player of players in {@link GameData}
     */
    public void winLottery(int playerIndex) {
        players.get(playerIndex).addMoney(lotteryAmount);
        lotteryAmount = 0;
    }

    /**
     * Buy a lotteryticket for given playerIndex and add price to lotteryAmount (win amount)
     *
     * @param playerIndex index of player of players in {@link GameData}
     * @param price for the lottery ticket
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
        Field curField = fields[player.getCurrentFieldIndex()];
        int nextFieldIndex = player.popFromMovePath();
        if (curField.getNextField() == nextFieldIndex) {
            Log.info("GameData-move", "getNextField");
        } else if (curField.getOptionalNextField() == nextFieldIndex) {
            Log.info("GameData-move", "getOptionalNextField");
        } else {
            Log.error("GameData-move", "Could not move player: " +currentPlayerTurn + " curF: "+curField.getFieldIndex() +" next: "+nextFieldIndex);
            return player.getPosition();
        }
        player.updateField(fields[nextFieldIndex]);
        return player.getPosition();
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

    public HashMap<Hotel, Integer> getHotels() {
        return hotels;
    }
}
