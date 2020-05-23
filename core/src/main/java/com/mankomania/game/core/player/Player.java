package com.mankomania.game.core.player;

import com.badlogic.gdx.math.Vector3;
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
     * Stocktype and amount of stock that a player has
     */
    private HashMap<Stock, Integer> stocks;


    public Player() {
        // empty kryonet
    }

    public Player(int startingFieldIndex, int connectionId, Vector3 position, int playerIndex) {
        money = 1000000;
        stocks = new HashMap<>();
        stocks.put(Stock.BRUCHSTAHLAG, 0);
        stocks.put(Stock.KURZSCHLUSSAG, 0);
        stocks.put(Stock.TROCKENOEL, 0);

        this.fieldIndex = startingFieldIndex;
        this.connectionId = connectionId;
        this.position = position;
        this.playerIndex = playerIndex;
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
        this.fieldIndex = field.getFieldIndex();
        position = field.getPositions()[playerIndex];
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

    public int getCurrentField() {
        return fieldIndex;
    }

    public int getConnectionId() {
        return connectionId;
    }
}
