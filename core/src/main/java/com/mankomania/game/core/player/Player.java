package com.mankomania.game.core.player;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.fields.types.Field;

import java.util.HashMap;

public class Player {

    /**
     * connectionId set by kryonet on connect of the client on the server
     */
    private int connectionId;

    /**
     * playerIndex in the players ArrayList in gameData
     */
    private int playerIndex;

    /**
     * current money a player has
     */
    private int money;

    /**
     * position on the board (is the position of the field pointed to by fieldIndex)
     */
    private Vector3 position;

    /**
     * current index of the field the player is on (gameData fields array)
     */
    private int fieldIndex;

    /**
     * Path to iterate over to move the player
     */
    private IntArray movePath;

    /**
     * Stocktype and amount of stock that a player has
     */
    private HashMap<Stock, Integer> stocks;

    /**
     * the index of the hotel field that the player owns or -1 if the player does not own a hotel field
     */
    private int boughtHotelFieldIndex;

    /**
     * int that indicates how often the player has already cheated, cheating is limited to certain number per game
     */
    private int cheatAmount;


    public Player() {
        // empty kryonet
    }

    public Player(int startingFieldIndex, int connectionId, Vector3 position, int playerIndex) {
        money = 1000000;
        stocks = new HashMap<>();
        movePath = new IntArray();
        stocks.put(Stock.BRUCHSTAHLAG, 0);
        stocks.put(Stock.KURZSCHLUSSAG, 0);
        stocks.put(Stock.TROCKENOEL, 0);

        this.fieldIndex = startingFieldIndex;
        this.connectionId = connectionId;
        this.position = position;
        this.playerIndex = playerIndex;
        this.boughtHotelFieldIndex = -1;
        this.cheatAmount = 0;
    }

    public void addToMovePath(int fieldIndex) {
        movePath.add(fieldIndex);
    }

    public int popFromMovePath() {
        return movePath.removeIndex(0);
    }

    public boolean isMovePathEmpty() {
        return movePath.isEmpty();
    }

    public void addToMovePath(IntArray moves) {
        movePath.addAll(moves);
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void buyStock(Stock stock, int amount) {
        int curr = stocks.get(stock);
        stocks.put(stock, curr + amount);
    }

    public void resetStocks() {
        stocks.put(Stock.BRUCHSTAHLAG, 0);
        stocks.put(Stock.KURZSCHLUSSAG, 0);
        stocks.put(Stock.TROCKENOEL, 0);
    }

    public void sellAllStock(Stock stock) {
        stocks.put(stock, 0);
    }

    public void sellSomeStock(Stock stock, int amount) {
        int curr = stocks.get(stock);
        if (amount > curr) {
            stocks.put(stock, 0);
        } else {
            stocks.put(stock, curr - amount);
        }
    }

    public void updateField(Field field) {
        fieldIndex = field.getFieldIndex();
        position = field.getPositions()[playerIndex];
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCheatAmount() {
        return cheatAmount;
    }

    public void addCheatAmount() {
        this.cheatAmount++;
    }

    public void updateField_S(Field field) {
        fieldIndex = field.getFieldIndex();
    }

    public int getAmountOfStock(Stock stock) {
        return stocks.get(stock);
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void loseMoney(int amount) {
        money -= amount;
    }

    public int getCurrentFieldIndex() {
        return fieldIndex;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public int getBoughtHotelFieldIndex() {
        return boughtHotelFieldIndex;
    }

    public void setBoughtHotelFieldIndex(int boughtHotelFieldIndex) {
        this.boughtHotelFieldIndex = boughtHotelFieldIndex;
    }

    /**
     * Update the Player without overriding object references
     *
     * @param player {@link Player}
     */
    public void update(Player player) {
        if (connectionId == player.connectionId && playerIndex == player.playerIndex) {
            money = player.money;
            stocks.clear();
            stocks.putAll(player.stocks);
            // set the bought hotel to be synced as well
            boughtHotelFieldIndex = player.boughtHotelFieldIndex;
        } else {
            Log.error("updatePlayer", "Tried to update wrong player!!!");
        }
    }

    public void payToPlayer(Player player, int amount) {
        money -= amount;
        player.addMoney(amount);
    }

    @Override
    public String toString() {
        return "P" + playerIndex + ": $ " + money +
                "\n\t" + Stock.BRUCHSTAHLAG + ": " + stocks.get(Stock.BRUCHSTAHLAG) +
                "\n\t" + Stock.TROCKENOEL + ": " + stocks.get(Stock.TROCKENOEL) +
                "\n\t" + Stock.KURZSCHLUSSAG + ": " + stocks.get(Stock.KURZSCHLUSSAG);
    }
}
